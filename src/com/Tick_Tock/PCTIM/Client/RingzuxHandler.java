package com.Tick_Tock.PCTIM.Client;
import io.netty.channel.*;
import com.Tick_Tock.PCTIM.Utils.*;
import io.netty.buffer.*;
import java.net.*;
import java.util.*;
import com.Tick_Tock.PCTIM.*;
import com.Tick_Tock.PCTIM.Package.*;
import java.util.concurrent.*;
import com.Tick_Tock.PCTIM.Tlv.*;
import com.Tick_Tock.PCTIM.Sdk.*;
import com.Tick_Tock.PCTIM.Robot.*;
import com.Tick_Tock.PCTIM.Message.*;

public class RingzuxHandler extends ChannelInboundHandlerAdapter
{
	private ExecutorService threadpool = new ThreadPoolExecutor(50, 50, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

	private QQRobot robot;

	public RingzuxHandler(QQUser user)
	{
		this.user = user;
	}

	public RingzuxHandler()
	{

	}
	private QQUser user;

	private ChannelHandlerContext ctx;

	@Override public void exceptionCaught(io.netty.channel.ChannelHandlerContext ctx, java.lang.Throwable cause) throws java.lang.Exception
	{
		cause.printStackTrace();
	}

	@Override public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
        System.out.println("Tcp已断开");
    }

	@Override public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		this.ctx = ctx;
		if (this.user == null)
		{
			System.out.println("通道连接成功，开始登陆");
			String qq =Util.readRecord("account");
			String password = Util.readRecord("password");
			byte[] passwordmd5=new byte[0];
			Scanner sc;
			String account;
			String passwd;
			if (qq == null || password == null)
			{
				sc = new Scanner(System.in); 
				System.out.println("请输入qq账号");
				account = sc.nextLine();
				sc = new Scanner(System.in); 
				System.out.println("请输入qq密码");
				passwd = sc.nextLine();
				this.user = new QQUser(Long.parseLong(account), Util.MD5(passwd));
			}
			else
			{
				passwordmd5 = Util.str_to_byte(password);
				this.user = new QQUser(Long.parseLong(qq), passwordmd5);
			}
		}
		user.txprotocol.serverAddres = ((InetSocketAddress)ctx.channel().remoteAddress()).getHostString();
		user.txprotocol.serverPort = (short) ((InetSocketAddress)ctx.channel().remoteAddress()).getPort();
		//System.out.println(user.TXProtocol.DwServerIP);
		
		byte[] data = SendPackage.get0825(user);
		this.send(data);
	}

	@Override public void channelRead(ChannelHandlerContext ctx, final Object buf)
	{
		if (buf instanceof ByteBuf)
		{
			this.threadpool.submit(
				new Runnable(){
					@Override public void run()
					{
						try
						{
							distruct_packet((ByteBuf)buf);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			);
		}
	}

	private void distruct_packet(ByteBuf buf)
	{
		ByteReader reader = new ByteReader(buf);
		try
		{
			reader.readBytes(2);//长度
			reader.readBytes(1);
			reader.readBytes(2);
			byte[] Command = reader.readBytes(2);
			String command = Util.byte2HexString(Command);
			int Sequence = reader.readShort();
			reader.readBytes(4);
			reader.readBytes(3);
			byte[] body_encrypted = reader.readBytes(reader.restBytesCount() - 1);
			System.out.println("[接收包] 命令: " + command);
			TeaCryptor crypter = new TeaCryptor();
			switch (command.replace(" ", ""))
			{
				case "0825":{
						byte[] body_decrypted = crypter.decrypt(body_encrypted, user.packet0825Key);
						reader.update(body_decrypted);
						byte header = reader.readBytes(1)[0];
						this.parse_tlv(reader.readRestBytes(), user);
						if (header == -2)
						{
							System.out.println("重定向到:" + this.user.txprotocol.redirectedAddress);
							this.user.txprotocol.redirectionCount += 1;
							this.user.txprotocol.pingType =0x01;
							this.user.isLoginRedirected = true;
							this.ctx.close();
							RingzuxClient client = new RingzuxClient(443, this.user.txprotocol.redirectedAddress, user);
							new Thread(client).start();
						}
						else
						{
							System.out.println("服务器连接成功,开始登陆");
							byte[] data = SendPackage.get0836(this.user, false);
							this.send(data);
						}
					}break;
				case "0836":{
						byte[] body_decrypted = crypter.decrypt(body_encrypted, this.user.txprotocol.ecdhShareKey);
						//System.out.println(Util.byte2HexString(body_decrypted));
						reader.update(body_decrypted);
						reader.readBytes(1);
						int header = reader.readBytes(1)[0];
						byte[] server_publickey = reader.readBytesByShortLength();
						byte[] server_sharekey = user.ecdh.consult(server_publickey);
						body_decrypted = crypter.decrypt(reader.readRestBytes(), server_sharekey);
						//System.out.println(Util.byte2HexString(body_decrypted));
						if (header == -5 || header == 0 || header == 1 || header == 51 ||
							header == 52 || header == 63 || header == 248 ||
							header == 249 || header == 250 || header == 251 ||
							header == 254 || header == 15 || header == 255)
						{
							byte[] h = reader.readRestBytes();
							//System.out.println(Util.byte2HexString(h));
							this.parse_tlv(h, user);
						}
						else
						{
							//System.out.println(Util.byte2HexString(body_decrypted));
							body_decrypted = crypter.decrypt(body_decrypted, user.txprotocol.tgtgtKey);
							reader.update(body_decrypted);
							header = reader.readBytes(1)[0];
							this.parse_tlv(reader.readRestBytes(), user);
						}
						if (header == 52)
						{
							System.out.println("密码错误");
							System.exit(100);
						}
						else if (header == -5)
						{
							byte[] data = SendPackage.get00ba(this.user, "");
							this.send(data);
						}
						else if (header == 0)
						{
							System.out.println("成功获取用户信息: Nick: " + this.user.nickName + " Age: " + this.user.age + " Sex: " + this.user.sex);
							this.user.isLogined = true;
							this.user.loginTime = new Date().getTime();
							byte[] data = SendPackage.get0828(this.user);
							this.send(data);
						}
						else
						{
							byte[] data = SendPackage.get0836(this.user, true);
							this.send(data);
						}

					}break;
				case "0828":{
						byte[] body_decrypted = crypter.decrypt(body_encrypted, user.txprotocol.tgtGtKey);
						reader.update(body_decrypted);
						reader.readBytes(1);
						this.parse_tlv(reader.readRestBytes(), user);		
						byte[] data = SendPackage.get00ec(this.user, user.txprotocol.Online);
						this.send(data);
						this.startHeatBeat();
						this.robot = new QQRobot(this, user);
						Util.writeRecord("account", String.valueOf(this.user.uin));
						Util.writeRecord("password", Util.byte2HexString(this.user.md51));
					}break;
				case "00EC":{
						byte[] data = SendPackage.get001d(this.user);
						this.send(data);
					}break;
				case "001D":{
						byte[] body_decrypted = crypter.decrypt(body_encrypted, user.txprotocol.sessionKey);
						reader.update(body_decrypted);
						reader.readBytes(4);
						this.user.skey = new String(reader.readBytes(10));
						//System.out.println(this.user.userskey);
					}break;
				case "0017":{
						ByteReader message_datafactory = null;
						try
						{
							byte[] body_decrypted = crypter.decrypt(body_encrypted, user.txprotocol.sessionKey);
							reader.update(body_decrypted);
							byte[] Message_To_Respone = reader.readBytes(16);
							reader.readerIndex(0);
							byte[] data_to_send = SendPackage.get0017(this.user, Message_To_Respone, Sequence);
							this.send(data_to_send);
							GroupMessage qqmessage = new GroupMessage();
							long Target_uin = reader.readInt();
							qqmessage.selfUin = reader.readInt();
							reader.readBytes(10);
							int type = reader.readShort();
							reader.readBytes(2);
							reader.readBytesByShortLength();
							qqmessage.groupUin = reader.readInt();
							byte[] flag = reader.readBytes(1);

							message_datafactory = new ByteReader(reader.readRestBytes());

							switch (type)
							{
								case 0x52 : // 群消息、被拉进/踢出群
									{
										if (flag[0] == 0x01)
										{
											qqmessage.senderUin = message_datafactory.readInt();
											//发消息人的QQ
											qqmessage.messageIndex = message_datafactory.readBytes(4); //姑且叫消息索引吧
											message_datafactory.readInt(); //接收时间  
											message_datafactory.readBytes(24);
											qqmessage.time = message_datafactory.readInt() * 1000; //发送时间 
											qqmessage.messageId = message_datafactory.readInt(); //id
											message_datafactory.readBytes(8);
											qqmessage.font = message_datafactory.readStringByShortLength();//字体
											message_datafactory.readBytes(2);
											byte[] rich_data = message_datafactory.readRestBytes();
											parseRichText(qqmessage, rich_data);
											if (qqmessage.senderUin != 0 && qqmessage.senderUin != user.uin)
											{
												System.out.println("[群消息] 接收: 群号: " + ((GroupMessage)qqmessage).groupUin + " 发送者: " + ((GroupMessage)qqmessage).senderName + " 内容: " + qqmessage.toString());
												this.robot.call(qqmessage);
											}
										}
										break;
									}
								case 0x21:
								case 0x22:
									{
										message_datafactory.readBytes(5);
										qqmessage.senderUin = message_datafactory.readInt(); // 邀请人/踢人QQ
										break;
									}
								case 0x2C:
									{


										break;
									}
								default:
									{
										break;
									}
							}

						}
						catch (Exception e)
						{
							
						}
						finally
						{
							if(message_datafactory!=null){
								message_datafactory.destroy();
							}
						}
					}break;
				case "00BA":{
						byte[] body_decrypted = crypter.decrypt(body_encrypted, this.user.packet00BaKey);
						reader.update(body_decrypted);
						byte VerifyType = reader.readBytes(1)[0];
						reader.readShort();
						byte Status = reader.readBytes(1)[0];
						reader.readBytes(4);
						user.txprotocol.sigImage = reader.readBytesByShortLength();
						if (VerifyType == 0x13)
						{
							byte[] VerifyCode = reader.readBytesByShortLength();

							byte VerifyCommand = reader.readBytes(1)[0];
							if (VerifyCommand == 0x00)
							{
								VerifyCommand = reader.readBytes(1)[0];
							}
							else
							{
								reader.readBytes(1);
							}

							if (user.packet00BaVerifyCode.length == 0 || user.packet00BaVerifyCode == null)
							{
								user.packet00BaVerifyCode = VerifyCode;
							}
							else
							{
								ByteBuilder resultArr = new ByteBuilder();
								resultArr.writeBytes(user.packet00BaVerifyCode);
								resultArr.writeBytes(VerifyCode);
								user.packet00BaVerifyCode = resultArr.getDataAndDestroy();
							}
							user.txprotocol.verificationImageToken = reader.readBytesByShortLength();
							reader.readBytesByShortLength();
						}
						else if (VerifyType == 0x14)
						{

						}
						if (Status == 0x0)
						{
							byte[] data = SendPackage.get0836(this.user, true);
							this.send(data);
							return;
						}
						if (Util.isvalidimg(this.user.packet00BaVerifyCode))
						{
							String textimg = Util.gettextimg(this.user.packet00BaVerifyCode);
							System.out.println(textimg);
							//此时验证码已获取，开始输入验证码
							String code = "";
							Scanner sc = new Scanner(System.in); 
							code = sc.nextLine();
							if (code == null || code.isEmpty() || code.equals(""))
							{
								code = "TICK";
							}
							System.out.println(code);
							this.send(SendPackage.get00ba(this.user, code));
						}
						else
						{
							this.send(SendPackage.get00ba(this.user, ""));
						}
					}break;
				case "0388":{
						PictureKeyStore keystore = new PictureKeyStore();
						byte[] body_decrypted = crypter.decrypt(body_encrypted, this.user.txprotocol.sessionKey);
						reader.update(body_decrypted);
						byte last1 = 0;
						byte last2 = 0;
						byte last = 0;
						byte[]_ukey =null;
						while (reader.hasMore())
						{
							last2 = last1;
							last1 = last;
							last = reader.readBytes(1)[0];
							if (last == (byte)0x01 && last1 == (byte)0x80 && last2 == (byte) 0x42)
							{
								_ukey = reader.readBytes(128);
								break;
							}
						}
						PictureStore store = null;
						if (_ukey == null)
						{
							for (PictureStore onestore: user.imageStoreCache)
							{
								if (onestore.pictureId == Sequence)
								{
									store = onestore;
									user.imageStoreCache.remove(onestore);
									break;
								}
							}
						}
						else
						{
							keystore.ukey = _ukey;
							store = Util.uploadImage(keystore, user, Sequence);
						}
						byte[] data_to_send = SendPackage.sendpic(this.user, store.fileName, store.groupUin);
						this.send(data_to_send);
					}break;
				case "00CE":{
					FriendMessage qqmessage = new FriendMessage();
						byte[] body_decrypted = crypter.decrypt(body_encrypted, this.user.txprotocol.sessionKey);
						reader.update(body_decrypted);
						byte[] Message_To_Respone = reader.readBytes(16);
						reader.readerIndex(0);
						qqmessage.senderUin = reader.readInt();
						long Friend_Message_QQ = qqmessage.senderUin;
						qqmessage.selfUin = reader.readInt();//自己的QQ
						reader.readBytes(10);
						reader.readBytes(2);
						reader.readShort();
						reader.readBytesByShortLength(); 
						reader.readShort(); //消息来源QQ的版本号
						reader.readBytes(4); //FromQQ
						reader.readBytes(4); //自己的QQ
						reader.readBytes(20);
						qqmessage.time = reader.readInt()*1000;
						reader.readShort(); //00
						byte[] Friend_Message_TIME =  reader.readBytes(4); //MessageDateTime
						reader.readBytes(5); //00
						reader.readBytes(3);
						reader.readBytes(5); //00
						reader.readBytes(4); //MessageDateTime
						qqmessage.messageId = reader.readInt(); //id
						reader.readBytes(8);
						qqmessage.font = reader.readStringByShortLength();
						reader.readBytes(1);
						reader.readBytes(1);
						byte[] data_to_send = SendPackage.get00ce(this.user, Message_To_Respone, Sequence);
						this.send(data_to_send);
						data_to_send = SendPackage.get0319(this.user, Friend_Message_QQ, Friend_Message_TIME);
						this.send(data_to_send);
						parseRichText(qqmessage,reader.readRestBytes());
						if (qqmessage.senderUin != 0 && qqmessage.senderUin != user.uin)
						{
							System.out.println("[好友消息] 接收: 发送者: " + qqmessage.senderUin  + " 内容: " + qqmessage.toString());
							this.robot.call(qqmessage);
						}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			reader.destroy();
		}
	}

	private void startHeatBeat()
	{
		this.ctx.executor().scheduleAtFixedRate(new Runnable(){
				@Override public void run()
				{
				    send(SendPackage.get0058(user));
				}
			}, 0, 100, TimeUnit.SECONDS);
	}
	
	public static void parseRichText(QQMessage qqmessage, byte[] rich_data)
	{
		ByteReader reader = new ByteReader(rich_data);

		ByteReader reader2 = new ByteReader(new byte[1]);
		try
		{
			while (reader.hasMore())
			{
				int messageType = reader.readBytes(1)[0];

				int messageLength = reader.readShort();

				byte[] message = reader.readBytes(messageLength);

				switch (messageType)
				{
					case 1:{//文本，艾特
							reader2.update(message);
							reader2.readBytes(1);
							int textlength = reader2.readShort();
							String text = reader2.readString(textlength);
							if (text.startsWith("@") && messageLength - textlength == 19)
							{
								reader2.readBytes(10);
								((GroupMessage)qqmessage).addAt(new AtStore().setAtName(text).setTargetUin(reader2.readInt()));
							}
							else
							{
								qqmessage.addText(text);
							}
						}break;
					case 10:{//语音
							reader2.update(message);
							reader2.readBytes(1);
							int textlength = reader2.readShort();
							String text = reader2.readString(textlength);
							qqmessage.addVoice(text);
						}break;
					case 3:{//图片
							reader2.update(message);
							reader2.readBytes(1);
							int textlength = reader2.readShort();
							String text = reader2.readString(textlength);
							qqmessage.addImage(text);
						}break;
					case 24:{//文件
							reader2.update(message);
							reader2.readBytes(1);
							int textlength = reader2.readShort();
							String text = reader2.readString(textlength);
							qqmessage.addFile(text);
						}break;
					case 20:{//xml
							reader2.update(message);
							reader2.readBytes(1);
							reader2.update(reader2.readBytesByShortLength());
							reader2.readBytes(1);
							qqmessage.addXml(new String(ZLibUtils.decompress(reader2.readRestBytes())));
						}break;
					case 18:{//群名片
							reader2.update(message);
							reader2.readBytes(15);
							((GroupMessage)qqmessage).senderName = reader2.readStringByShortLength();
						}
				}
			}
		}
		finally
		{
			reader.destroy();
			reader2.destroy();
		}


	}

	

	private void parse_tlv(byte[] data, QQUser user)
	{
		List<Tlv> tlvs = Tlv.parseTlv(data);
		for (Tlv tlv: tlvs)
		{
			TlvParser.parsetvl(tlv, user);
		}
	}

	public void send(byte[] data)
	{
		ByteBuf buf = Unpooled.directBuffer();
		buf.writeBytes(data);
		ctx.writeAndFlush(buf);
		//aa.release();
	}
	
	
	
	


}
