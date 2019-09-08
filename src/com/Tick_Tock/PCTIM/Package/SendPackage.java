package com.Tick_Tock.PCTIM.Package;
import com.Tick_Tock.PCTIM.Utils.*;
import com.Tick_Tock.PCTIM.*;
import com.Tick_Tock.PCTIM.TLV.*;
import java.util.*;
import java.awt.image.*;
import com.Tick_Tock.PCTIM.Message.*;
import com.Tick_Tock.PCTIM.sdk.*;
import java.io.*;

public class SendPackage
{
	protected static int _seq = 0x3100; // (char)Util.Random.Next();
	protected static byte[] body_end = {0x03};

	public static byte[] get001d(QQUser user)
	{
		Util.log("[发送包] 命令: 00 1d");
		ByteBuilder builder = new ByteBuilder();


		builder.writeBytes(QQGlobal.QQHeaderBasicFamily);
		builder.writeBytes(user.TXProtocol.CMainVer);

		builder.writeBytes(user.TXProtocol.CSubVer);
		builder.writeBytes(Util.str_to_byte("00 1d"));
		builder.writeShort(GetNextSeq());

		builder.writeInt(user.QQ);

		builder.writeBytes(user.TXProtocol.XxooA);
		builder.writeBytes(user.TXProtocol.DwClientType);
		builder.writeBytes(user.TXProtocol.DwPubNo);
		builder.writeBytes(user.TXProtocol.XxooD);

		ByteBuilder body_builder = new ByteBuilder() ;
		body_builder.writeBytes(Util.str_to_byte(" 33 00 05 00 08 74 2E 71 71 2E 63 6F 6D 00 0A 71 75 6E 2E 71 71 2E 63 6F 6D 00 0C 71 7A 6F 6E 65 2E 71 71 2E 63 6F 6D 00 0C 6A 75 62 61 6F 2E 71 71 2E 63 6F 6D 00 09 6B 65 2E 71 71 2E 63 6F 6D "));
		
		byte[] tlv_data = body_builder.getDataAndDestroy();
		Crypter crypter = new Crypter();
		byte[] result = crypter.encrypt(tlv_data,user.TXProtocol.SessionKey);
		builder.writeBytes(result);
		builder.writeBytes(body_end);
		builder.rewriteShort(builder.totalcount()+2);
		return builder.getDataAndDestroy();
	}
	
	
	
	
	
	public static byte[] get00ba(QQUser user, String code)
	{
		Util.log("[发送包] 命令: 00 ba");
		ByteBuilder builder = new ByteBuilder();


		builder.writeBytes(QQGlobal.QQHeaderBasicFamily);
		builder.writeBytes(user.TXProtocol.CMainVer);

		builder.writeBytes(user.TXProtocol.CSubVer);
		builder.writeBytes(Util.str_to_byte("00 ba"));
		builder.writeShort(GetNextSeq());
		
		builder.writeInt(user.QQ);

		builder.writeBytes(user.TXProtocol.XxooA);
		builder.writeBytes(user.TXProtocol.DwClientType);
		builder.writeBytes(user.TXProtocol.DwPubNo);
		builder.writeBytes(user.TXProtocol.XxooD);
		builder.writeBytes(user.QQPacket00BaKey);

		ByteBuilder body_builder = new ByteBuilder() ;
		body_builder.writeBytes(Util.str_to_byte("00 02 00 00 08 04 01 E0"));
		body_builder.writeBytes(user.TXProtocol.DwSsoVersion);
		body_builder.writeBytes(user.TXProtocol.DwServiceId);
		body_builder.writeBytes(user.TXProtocol.DwClientVer);
		body_builder.writeByte((byte) 0x00);
		body_builder.writeBytesByShortLength(user.TXProtocol.BufSigClientAddr);
		body_builder.writeBytes(new byte[] {0x01, 0x02});
		body_builder.writeBytesByShortLength(user.TXProtocol.BufDhPublicKey);
		if (code.equals(""))
		{
			body_builder.writeBytes(new byte[] {0x13, 0x00, 0x05, 0x00, 0x00, 0x00, 0x00});
			body_builder.writeByte(user.QQPacket00BaSequence);
			if (user.TXProtocol.PngToken == null || user.TXProtocol.PngToken.length == 0)
			{
			     body_builder.writeByte((byte) 0x00);
			}
			else
			{
				body_builder.writeBytesByShortLength(user.TXProtocol.PngToken);
			}
		}
		else
		{
			byte[] verifyCodeBytes = code.getBytes();
			body_builder.writeBytes(new byte[] {0x14, 0x00, 0x05, 0x00, 0x00, 0x00, 0x00});
			body_builder.writeShort(verifyCodeBytes.length);///?????
		    body_builder.writeBytes(verifyCodeBytes);
			body_builder.writeBytesByShortLength(user.TXProtocol.BufSigPic);
			//输入验证码后清空图片流
			user.QQPacket00BaVerifyCode = new byte[] { };
			user.QQPacket00BaSequence=0x1;
		}

		body_builder.writeBytesByShortLength(user.QQPacket00BaFixKey);
		byte[] tlv_data = body_builder.getDataAndDestroy();
		Crypter crypter = new Crypter();
		byte[] result = crypter.encrypt(tlv_data,user.QQPacket00BaKey);
		builder.writeBytes(result);
		builder.writeBytes(body_end);
		builder.rewriteShort(builder.totalcount()+2);
		user.QQPacket00BaSequence+=1;
		return builder.getDataAndDestroy();
	}
	
	public static byte[] get0825(QQUser user){
		System.out.println("[发送包] 命令: 08 25");
		ByteBuilder builder = new ByteBuilder();
		builder.writeBytes(QQGlobal.QQHeaderBasicFamily);
		builder.writeBytes(user.TXProtocol.CMainVer);
		builder.writeBytes(user.TXProtocol.CSubVer);
		builder.writeBytes(new byte[]{0x08,0x25});
		builder.writeShort(GetNextSeq());
		builder.writeInt(user.QQ);
		builder.writeBytes(user.TXProtocol.XxooA);
		builder.writeBytes(user.TXProtocol.DwClientType);
		builder.writeBytes(user.TXProtocol.DwPubNo);
		builder.writeBytes(user.TXProtocol.XxooD);
		builder.writeBytes(user.QQPacket0825Key);
		ByteBuilder tlv_builder = new ByteBuilder() ;
		byte[] tlv0018 = TLVFactory.tlv0018(user);
		byte[] tlv0309 = TLVFactory.tlv0309(user);
		byte[] tlv0036 = TLVFactory.tlv0036(2);
		byte[] tlv0114 = TLVFactory.tlv0114(user);
		tlv_builder.writeBytes(tlv0018);
		tlv_builder.writeBytes(tlv0309);
		tlv_builder.writeBytes(tlv0036);
		tlv_builder.writeBytes(tlv0114);
		byte[] tlv_data = tlv_builder.getDataAndDestroy();
		Crypter crypter = new Crypter();
		byte[] result = crypter.encrypt(tlv_data,user.QQPacket0825Key);
		builder.writeBytes(result);
		builder.writeBytes(body_end);
		builder.rewriteShort(builder.totalcount()+2);///////*****
		//System.out.println(Util.byte2HexString(builder.getdata()));
		return builder.getDataAndDestroy();
	}

	public static byte[] get0836(QQUser user,boolean need_verifycode){
		System.out.println("[发送包] 命令: 08 36");
		ByteBuilder builder = new ByteBuilder();

		byte[] tlv0110 = null;
		byte[] tlv0032 = null;
		builder.writeBytes(QQGlobal.QQHeaderBasicFamily);
		builder.writeBytes(user.TXProtocol.CMainVer);

		builder.writeBytes(user.TXProtocol.CSubVer);
		builder.writeBytes(new byte[]{0x08,0x36});
		builder.writeShort(GetNextSeq());
		builder.writeInt(user.QQ);
		builder.writeBytes(user.TXProtocol.XxooA);
		builder.writeBytes(user.TXProtocol.DwClientType);
		builder.writeBytes(user.TXProtocol.DwPubNo);
		builder.writeBytes(user.TXProtocol.XxooD);

		builder.writeBytes(user.TXProtocol.SubVer);
		builder.writeBytes(user.TXProtocol.EcdhVer);
		builder.writeBytesByShortLength(user.TXProtocol.BufDhPublicKey);
		builder.writeBytes(new byte[] { 0x00, 0x00, 0x00, 0x10 });
		builder.writeBytes(user.QQPacket0836Key1);

		ByteBuilder  tlv_builder = new ByteBuilder();
		byte[] tlv0112 = TLVFactory.tlv0112(user);
		
		byte[] tlv030f = TLVFactory.tlv030f(user);
		byte[] tlv0005 = TLVFactory.tlv0005(user);
		byte[] tlv0006 = TLVFactory.tlv0006(user);
		byte[] tlv0015 = TLVFactory.tlv0015(user);
		byte[] tlv001a = TLVFactory.tlv001a(tlv0015,user);
		byte[] tlv0018 = TLVFactory.tlv0018(user);
		byte[] tlv0103 = TLVFactory.tlv0103(user);
		if(need_verifycode){
			/*
			 BodyWriter.Write(new TLV0110().Get_Tlv(User));
			 BodyWriter.Write(new TLV0032().Get_Tlv(User));
			 */
			tlv0110 = TLVFactory.tlv0110(user);
			tlv0032 = TLVFactory.tlv0032(user);
		}
		byte[] tlv0312= TLVFactory.tlv0312();
		byte[] tlv0508 = TLVFactory.tlv0508();
		byte[] tlv0313= TLVFactory.tlv0313(user);
		byte[] tlv0102= TLVFactory.tlv0102(user);

		tlv_builder.writeBytes(tlv0112);
		tlv_builder.writeBytes(tlv030f);
		tlv_builder.writeBytes(tlv0005);
		tlv_builder.writeBytes(tlv0006);
		tlv_builder.writeBytes(tlv0015);
		tlv_builder.writeBytes(tlv001a);
		tlv_builder.writeBytes(tlv0018);
		tlv_builder.writeBytes(tlv0103);
		if(need_verifycode){
			tlv_builder.writeBytes(tlv0110);
			//tlv_builder.writebytes(tlv0032);
		}
		tlv_builder.writeBytes(tlv0312);
		tlv_builder.writeBytes(tlv0508);
		tlv_builder.writeBytes(tlv0313);
		tlv_builder.writeBytes(tlv0102);

		//tlv_builder.writebytes(crckey);
		//tlv_builder.writebytes(crccode);
		byte[] tlv_data = tlv_builder.getDataAndDestroy();
		Crypter crypter = new Crypter();
		byte[] result = crypter.encrypt(tlv_data,user.TXProtocol.BufDhShareKey);
		builder.writeBytes(result);

		builder.writeBytes(body_end);
		builder.rewriteShort(builder.totalcount()+2);
		return builder.getDataAndDestroy();
	}

	public static byte[] get0828(QQUser user){
		System.out.println("[发送包] 命令: 08 28");
		ByteBuilder builder = new ByteBuilder();


		builder.writeBytes(QQGlobal.QQHeaderBasicFamily);
		builder.writeBytes(user.TXProtocol.CMainVer);
		builder.writeBytes(user.TXProtocol.CSubVer);
		builder.writeBytes( new byte[]{0x08,0x28});
		builder.writeShort(GetNextSeq());
		builder.writeInt(user.QQ);
		builder.writeBytes(new byte[] { 0x02, 0x00, 0x00});
		builder.writeBytes(user.TXProtocol.DwClientType);
		builder.writeBytes(user.TXProtocol.DwPubNo);
		builder.writeBytes(new byte[] { 0x00, 0x30, 0x00, 0x3a });
		builder.writeBytesByShortLength(user.TXProtocol.BufSigSession);

		ByteBuilder tlv_builder = new ByteBuilder();

		byte[] tlv0007 = TLVFactory.tlv0007(user);
		byte[] tlv000c = TLVFactory.tlv000c(user);
		byte[] tlv0015 = TLVFactory.tlv0015(user);
		byte[] tlv0036 = TLVFactory.tlv0036(2);
		byte[] tlv0018 = TLVFactory.tlv0018(user);
		byte[] tlv001f = TLVFactory.tlv001f(user);
		byte[] tlv0105 = TLVFactory.tlv0105(user);
		byte[] tlv010b = TLVFactory.tlv010b(user);
		byte[] tlv002d = TLVFactory.tlv002d(user);

		tlv_builder.writeBytes(tlv0007);
		tlv_builder.writeBytes(tlv000c);
		tlv_builder.writeBytes(tlv0015);
		tlv_builder.writeBytes(tlv0036);
		tlv_builder.writeBytes(tlv0018);
		tlv_builder.writeBytes(tlv001f);
		tlv_builder.writeBytes(tlv0105);
		tlv_builder.writeBytes(tlv010b);
		tlv_builder.writeBytes(tlv002d);

		byte[] tlv_data = tlv_builder.getDataAndDestroy();
		Crypter crypter = new Crypter();
		byte[] result = crypter.encrypt(tlv_data,user.TXProtocol.BufSessionKey);
		builder.writeBytes(result);
		builder.writeBytes(body_end);
		builder.rewriteShort(builder.totalcount()+2);
		return builder.getDataAndDestroy();
	}
	
	
	
	
	

	public static byte[] get00ec(QQUser user,byte[] loginStatus){
		System.out.println("[发送包] 命令: 00 ec");
		ByteBuilder builder = new ByteBuilder();

		builder.writeBytes(QQGlobal.QQHeaderBasicFamily);
		builder.writeBytes(user.TXProtocol.CMainVer);
		builder.writeBytes(user.TXProtocol.CSubVer);
		builder.writeBytes( Util.str_to_byte("00 ec"));
		builder.writeShort(GetNextSeq());
		builder.writeInt(user.QQ);
		builder.writeBytes(user.TXProtocol.XxooA);
		builder.writeBytes(user.TXProtocol.DwClientType);
		builder.writeBytes(user.TXProtocol.DwPubNo);
		builder.writeBytes(user.TXProtocol.XxooD);

		ByteBuilder body_builder=new ByteBuilder();

		body_builder.writeBytes(new byte[] { 0x01, 0x00 });
	    body_builder.writeBytes(loginStatus);
	    body_builder.writeBytes(new byte[] { 0x00, 0x01, 0x00, 0x01, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00 });

		Crypter crypter = new Crypter();
		byte[] result = crypter.encrypt(body_builder.getDataAndDestroy(),user.TXProtocol.SessionKey);
		builder.writeBytes(result);
		builder.writeBytes(body_end);
		builder.rewriteShort(builder.totalcount()+2);
		return builder.getDataAndDestroy();

	}

	public static byte[] get0058(QQUser user){
		System.out.println("[发送包] 命令: 00 58");
		ByteBuilder builder = new ByteBuilder();

		builder.writeBytes(QQGlobal.QQHeaderBasicFamily);
		builder.writeBytes(user.TXProtocol.CMainVer);
		builder.writeBytes(user.TXProtocol.CSubVer);
		builder.writeBytes(new byte[]{0x00,0x58});
		builder.writeShort(GetNextSeq());
		builder.writeInt(user.QQ);
		builder.writeBytes(user.TXProtocol.XxooA);
		builder.writeBytes(user.TXProtocol.DwClientType);
		builder.writeBytes(user.TXProtocol.DwPubNo);
		builder.writeBytes(user.TXProtocol.XxooD);

		ByteBuilder body_builder=new ByteBuilder();

		body_builder.writeInt(user.QQ);

		Crypter crypter = new Crypter();
		byte[] result = crypter.encrypt(body_builder.getDataAndDestroy(),user.TXProtocol.SessionKey);
		builder.writeBytes(result);
		builder.writeBytes(body_end);
		builder.rewriteShort(builder.totalcount()+2);
		return builder.getDataAndDestroy();

	}



	public static byte[] get0017(QQUser user,byte[] data_to_send,int seq)
	{
		System.out.println("[发送包] 命令: 00 17");
		ByteBuilder builder = new ByteBuilder();

		builder.writeBytes(QQGlobal.QQHeaderBasicFamily);
		builder.writeBytes(user.TXProtocol.CMainVer);
		builder.writeBytes(user.TXProtocol.CSubVer);
		builder.writeBytes(new byte[]{0x00,0x17});
		builder.writeShort(seq);
		builder.writeInt(user.QQ);
		builder.writeBytes(user.TXProtocol.XxooA);
		builder.writeBytes(user.TXProtocol.DwClientType);
		builder.writeBytes(user.TXProtocol.DwPubNo);
		builder.writeBytes(user.TXProtocol.XxooD);

		ByteBuilder body_builder=new ByteBuilder();

		body_builder.writeBytes(data_to_send);

		Crypter crypter = new Crypter();
		byte[] result = crypter.encrypt(body_builder.getDataAndDestroy(),user.TXProtocol.SessionKey);
		builder.writeBytes(result);
		builder.writeBytes(body_end);
		builder.rewriteShort(builder.totalcount()+2);
		return builder.getDataAndDestroy();
	}

	public static byte[] get00ce(QQUser user,byte[] data_to_send,int seq)
	{
		System.out.println("[发送包] 命令: 00 ce");
		ByteBuilder builder = new ByteBuilder();

		builder.writeBytes(QQGlobal.QQHeaderBasicFamily);
		builder.writeBytes(user.TXProtocol.CMainVer);
		builder.writeBytes(user.TXProtocol.CSubVer);
		builder.writeBytes(Util.str_to_byte("00ce"));
		builder.writeShort(seq);
		builder.writeInt(user.QQ);
		builder.writeBytes(user.TXProtocol.XxooA);
		builder.writeBytes(user.TXProtocol.DwClientType);
		builder.writeBytes(user.TXProtocol.DwPubNo);
		builder.writeBytes(user.TXProtocol.XxooD);

		ByteBuilder body_builder=new ByteBuilder();

		body_builder.writeBytes(data_to_send);

		Crypter crypter = new Crypter();
		byte[] result = crypter.encrypt(body_builder.getDataAndDestroy(),user.TXProtocol.SessionKey);
		builder.writeBytes(result);
		builder.writeBytes(body_end);
		builder.rewriteShort(builder.totalcount()+2);
		return builder.getDataAndDestroy();

	}


	public static byte[] get0319(QQUser user,long _recvQQ,byte[] MessageTime){
		Util.log("[发送包] 命令: 03 19");
		ByteBuilder builder = new ByteBuilder();
		builder.writeBytes(QQGlobal.QQHeaderBasicFamily);
		builder.writeBytes(user.TXProtocol.CMainVer);
		builder.writeBytes(user.TXProtocol.CSubVer);
		builder.writeBytes(Util.str_to_byte("0319"));
		builder.writeShort(GetNextSeq());
		builder.writeInt(user.QQ);
		builder.writeBytes(new byte[]
						   {
							   0x04, 0x00, 0x00, 0x00, 0x01, 0x01, 0x01, 0x00, 0x00, 0x68, 0x1C, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
							   0x00, 0x00
						   });
		ByteBuilder body_builder=new ByteBuilder();


		body_builder.writeBytes(new byte[] { 0x00, 0x00, 0x00, 0x07 });
		ByteBuilder data_builder=new ByteBuilder();

		data_builder.writeBytes(Util.str_to_byte("0A0C08"));
		data_builder.writeBytes(Util.str_to_byte(Util.PB_toLength(_recvQQ)));
		data_builder.writeByte((byte) 0x10);
		data_builder.writeBytes(
			Util.str_to_byte(
				Util.PB_toLength(Long.parseLong(Util.byte2HexString(MessageTime).replace(" ",""),16))));
		data_builder.writeBytes(new byte[] { 0x20, 0x00 });
		//数据长度

		body_builder.writeInt(body_builder.totalcount());
		body_builder.writeBytes(Util.str_to_byte("08011203980100"));
		//数据
		body_builder.writeBytes(data_builder.getDataAndDestroy());

		Crypter crypter = new Crypter();
		byte[] result = crypter.encrypt(body_builder.getDataAndDestroy(),user.TXProtocol.SessionKey);
		builder.writeBytes(result);
		builder.writeBytes(body_end);
		builder.rewriteShort(builder.totalcount()+2);
		return builder.getDataAndDestroy();
	}

	public static byte[] get0002(QQUser user,String message,long groupUin,int type){
		System.out.println("[发送包] 命令: 00 02");
		ByteBuilder builder = new ByteBuilder();
		builder.writeBytes(QQGlobal.QQHeaderBasicFamily);
		builder.writeBytes(user.TXProtocol.CMainVer);
		builder.writeBytes(user.TXProtocol.CSubVer);
		builder.writeBytes(new byte[]{0x00,0x02});
		builder.writeShort(GetNextSeq());
		builder.writeInt(user.QQ);
		builder.writeBytes(user.TXProtocol.XxooA);
		builder.writeBytes(user.TXProtocol.DwClientType);
		builder.writeBytes(user.TXProtocol.DwPubNo);
		builder.writeBytes(user.TXProtocol.XxooD);
		ByteBuilder body_builder=new ByteBuilder();
		long dateTime = Util.GetTimeSeconds(new Date());
		long group = Util.ConvertQQGroupId(groupUin);
		switch(type){
			case 0:
				{
					byte[] message_to_send = Util.constructmessage(user,message.getBytes());

				
					body_builder.writeByte((byte)0x2A);
					body_builder.writeInt(group);
					body_builder.writeShort(message_to_send.length + 50); // 字符串长度 + 56，但是_data里面包含了和长度有关的额外六个byte
					body_builder.writeBytes(new byte[]
											{
												0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x4D, 0x53,
												0x47, 0x00,
												0x00, 0x00, 0x00, 0x00
											});

					body_builder.writeInt(new Date().getTime()/1000);
					body_builder.writeBytes(Util.RandomKey(4));
					body_builder.writeBytes(Util.str_to_byte("0000000009008600"));
					body_builder.writeBytes(new byte[] { 0x00, 0x0C });
					body_builder.writeBytes(Util.str_to_byte("E5BEAEE8BDAFE99B85E9BB91"));
					body_builder.writeBytes(new byte[] { 0x00,0x00 });
					body_builder.writeBytes(message_to_send);
					break;
				}
			case 1:
				{
					byte[] message_to_send =ZLibUtils.compress(message.getBytes());

					body_builder.writeByte((byte)0x2A);
					body_builder.writeInt(group);
					body_builder.writeShort(message_to_send.length + 64);
					body_builder.writeBytes(new byte[]
											{
												0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x4D, 0x53,
												0x47, 0x00,
												0x00, 0x00, 0x00, 0x00
											});
					body_builder.writeInt(new Date().getTime()/1000);
					body_builder.writeBytes(Util.constructxmlmessage(user,message_to_send));
					break;

				}
			case 2:
				{

					break;
				}

		}
		Crypter crypter = new Crypter();
		byte[] result = crypter.encrypt(body_builder.getDataAndDestroy(),user.TXProtocol.SessionKey);
		builder.writeBytes(result);
		builder.writeBytes(body_end);
		builder.rewriteShort(builder.totalcount()+2);
		return builder.getDataAndDestroy();
	}


	public static byte[] sendpic(QQUser user,String message,long groupUin){
		System.out.println("[发送包] 命令: 00 02");
		ByteBuilder builder = new ByteBuilder();
		builder.writeBytes(QQGlobal.QQHeaderBasicFamily);
		builder.writeBytes(user.TXProtocol.CMainVer);
		builder.writeBytes(user.TXProtocol.CSubVer);
		builder.writeBytes(Util.str_to_byte("0002"));
		builder.writeShort(GetNextSeq());
		builder.writeInt(user.QQ);
		builder.writeBytes(Util.str_to_byte("02 00 00 00 01 01 01 00 00 68 20"));
		ByteBuilder body_builder=new ByteBuilder();
		long dateTime = Util.GetTimeSeconds(new Date());
		long group = Util.ConvertQQGroupId(groupUin);
		byte[] guid = ("{"+Util.GetMD5ToGuidHashFromFile(message) + "}."+message.split("[.]")[message.split("[.]").length-1]).getBytes();
		body_builder.writeByte ((byte)0x2A);
		body_builder.writeInt(group);
		body_builder.writeBytes (Util.str_to_byte("01 00 00 02 01 00 00 00 00 00 00 00 4D 53 47 00 00 00 00 00"));
		body_builder.writeInt(new Date().getTime()/1000);
		body_builder.writeBytes(Util.RandomKey(4));
		body_builder.writeBytes (Util.str_to_byte("00 00 00 00 09 00 86 00"));
		body_builder.writeBytes (Util.str_to_byte("00 0C"));
		body_builder.writeBytes (Util.str_to_byte("E5 BE AE E8 BD AF E9 9B 85 E9 BB 91"));
		body_builder.writeBytes (Util.str_to_byte("00 00 03 00 CB 02"));
		body_builder.writeBytes (Util.str_to_byte("00 2A"));
		body_builder.writeBytes (guid);
		body_builder.writeBytes (Util.str_to_byte("04 00 04"));
		body_builder.writeBytes (Util.str_to_byte("9B 53 B0 08 05 00 04 D9 8A 5A 70 06 00 04 00 00 00 50 07 00 01 43 08 00 00 09 00 01 01 0B 00 00 14 00 04 11 00 00 00 15 00 04 00 00 02 BC 16 00 04 00 00 02 BC 18 00 04 00 00 7D 5E FF 00 5C 15 36 20 39 32 6B 41 31 43 39 62 35 33 62 30 30 38 64 39 38 61 35 61 37 30"));
		body_builder.writeBytes (Util.str_to_byte("20 20 20 20 20 20 35 30 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20"));
		body_builder.writeBytes(guid);
		body_builder.writeByte ((byte)0x41);
		Crypter crypter = new Crypter();
		byte[] result = crypter.encrypt(body_builder.getDataAndDestroy(),user.TXProtocol.SessionKey);
		builder.writeBytes(result);
		builder.writeBytes(body_end);
		builder.rewriteShort(builder.totalcount()+2);
		return builder.getDataAndDestroy();
	}

	public static byte[] get00cd(QQUser user,String message,long friendUin,int type){
		System.out.println("[发送包] 命令: 00 cd");
		ByteBuilder builder = new ByteBuilder();
		builder.writeBytes(QQGlobal.QQHeaderBasicFamily);
		builder.writeBytes(user.TXProtocol.CMainVer);
		builder.writeBytes(user.TXProtocol.CSubVer);
		builder.writeBytes(Util.str_to_byte("00cd"));
		builder.writeShort(GetNextSeq());
		builder.writeInt(user.QQ);
		builder.writeBytes(user.TXProtocol.XxooA);
		builder.writeBytes(user.TXProtocol.DwClientType);
		builder.writeBytes(user.TXProtocol.DwPubNo);
		builder.writeBytes(user.TXProtocol.XxooD);

		ByteBuilder body_builder=new ByteBuilder();
		long dateTime = Util.GetTimeSeconds(new Date());
		byte[] md5 = user.TXProtocol.SessionKey;
		switch(type){
			case 0:
				{
					byte[] message_to_send = Util.constructmessage(user,message.getBytes());
					body_builder.writeInt(user.QQ);
					body_builder.writeInt(friendUin);
                    body_builder.writeBytes(new byte[]
											{
												0x00, 0x00, 0x00, 0x0D, 0x00, 0x01, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x00,
												0x01, 0x01
											});
                    body_builder.writeBytes(user.TXProtocol.CMainVer);
                    body_builder.writeBytes(user.TXProtocol.CSubVer);
                    body_builder.writeInt(user.QQ);
                    body_builder.writeInt(friendUin);
                    body_builder.writeBytes(md5);
                    body_builder.writeBytes(new byte[] { 0x00, 0x0B });
                    body_builder.writeBytes(Util.RandomKey(2));
                    body_builder.writeInt(dateTime);
                    body_builder.writeBytes(new byte[]
											{
												0x02, 0x34, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x4D,
												0x53, 0x47,
												0x00, 0x00, 0x00, 0x00, 0x00
											});
                    body_builder.writeInt(dateTime);
                    byte[] MessageId = Util.RandomKey(4);
                    body_builder.writeBytes(MessageId);
                    body_builder.writeBytes(Util.str_to_byte("0000000009008600"));
                    body_builder.writeBytes(new byte[] { 0x00, 0x06 });
                    body_builder.writeBytes(Util.str_to_byte("E5AE8BE4BD93"));
                    body_builder.writeBytes(new byte[] { 0x00, 0x00 });
                    body_builder.writeBytes(message_to_send);
                    break;
				}
			case 1:
				{
					byte[] message_to_send =ZLibUtils.compress(message.getBytes());
					body_builder.writeInt(user.QQ);
                    body_builder.writeInt(friendUin);
                    body_builder.writeBytes(new byte[] { 0x00, 0x00, 0x00, 0x08, 0x00, 0x01, 0x00, 0x04 });
                    body_builder.writeBytes(new byte[] { 0x00, 0x00, 0x00, 0x00 });
                    body_builder.writeBytes(Util.str_to_byte("370F"));
                    body_builder.writeInt(user.QQ);
                    body_builder.writeInt(friendUin);
                    body_builder.writeBytes(md5);
                    body_builder.writeBytes(Util.str_to_byte("000B"));
                    body_builder.writeBytes(Util.RandomKey(2));
                    body_builder.writeInt(dateTime);
                    body_builder.writeBytes(new byte[]
											{
												0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x4D,
												0x53, 0x47,
												0x00, 0x00, 0x00, 0x00, 0x00
											});
                    body_builder.writeInt(dateTime);
                    body_builder.writeBytes(Util.constructxmlmessage(user,message_to_send));
				}
		}


		Crypter crypter = new Crypter();
		byte[] result = crypter.encrypt(body_builder.getDataAndDestroy(),user.TXProtocol.SessionKey);
		builder.writeBytes(result);
		builder.writeBytes(body_end);
		builder.rewriteShort(builder.totalcount()+2);
		return builder.getDataAndDestroy();
	}

	public static byte[] get0388(QQUser user,String message,long groupUin){
		System.out.println("[发送包] 命令: 03 88");
		ByteBuilder builder = new ByteBuilder();
		builder.writeBytes(QQGlobal.QQHeaderBasicFamily);
		builder.writeBytes(user.TXProtocol.CMainVer);
		builder.writeBytes(user.TXProtocol.CSubVer);
		builder.writeBytes(Util.str_to_byte("0388"));
		int seq = GetNextSeq();
		builder.writeShort(seq);
		builder.writeInt(user.QQ);
		builder.writeBytes(Util.str_to_byte("04 00 00 00 01 01 01 00 00 68 20 00 00 00 00 00 00 00 00 "));
		ByteBuilder body_builder = new ByteBuilder();
	    BufferedImage img = Util.get_img(message);
		long width = img.getWidth(); // 图片的宽度
		long height = img.getHeight(); // 图片的高度
		byte[] img_byte = Util.bytefromfile(message);
		long img_length = Util.getfilelength(message);
		byte[] md5 = Util.MD5(img_byte);
		ByteBuilder img_builder=new ByteBuilder();
		img_builder.j(1,groupUin)
			.j(2, user.QQ)
			.j(3, 0)
			.j(4, md5)
			.j(5, img_length)
			.j(6, new byte[]{0x37,0x00,0x4D,0x00,0x32,0x00,0x25,0x00,0x4C,0x00,0x31,0x00,0x56,0x00,0x32,0x00,0x7B,0x00,0x39,0x00,0x30,0x00,0x29,0x00,0x52,0x00})
			.j(7, 1)
			.j(9, 1)
			.j(10, width)
			.j(11, height)
			.j(12, 4)
			.j(13, "26656".getBytes())
			.j(14, 0)
			.j(15, 3)
			.j(16, 0);
		byte[] tmp = c(3, img_builder.getDataAndDestroy());
		body_builder.writeBytes(new byte[] {0x00,0x00,0x00,0x07});
		body_builder.writeInt(tmp.length - 1);
		body_builder.writeBytes(new byte[]{0x08,0x01,0x12,0x03,(byte)0x98,0x01,0x01,0x10,0x01});
		body_builder.writeBytes(tmp);
		Crypter crypter = new Crypter();
		byte[] result = crypter.encrypt(body_builder.getDataAndDestroy(),user.TXProtocol.SessionKey);
		builder.writeBytes(result);
		builder.writeBytes(body_end);
		PictureStore store = new PictureStore();
		store.pictureId = seq;
		store.data = img_byte;
		store.groupUin =groupUin;
		store.fileName = message;
		user.imgs.add(store);
		builder.rewriteShort(builder.totalcount()+2);
		return builder.getDataAndDestroy();
	}


	public static byte[] c(int paramInt, byte... paramVarArgs)
	{
		byte[] arrayOfByte1 = a(paramVarArgs.length);
		byte[] arrayOfByte2 = a(paramInt << 3 | 0x2);
		byte[] arrayOfByte3 = new byte[paramVarArgs.length + arrayOfByte2.length + arrayOfByte1.length];
		System.arraycopy(arrayOfByte2, 0, arrayOfByte3, 0, arrayOfByte2.length);
		System.arraycopy(arrayOfByte1, 0, arrayOfByte3, arrayOfByte2.length, arrayOfByte1.length);
		System.arraycopy(paramVarArgs, 0, arrayOfByte3, arrayOfByte1.length + arrayOfByte2.length, paramVarArgs.length);
		return arrayOfByte3;
	}
	
	public static byte[] b(int paramInt, long paramLong)
	{
		byte[] arrayOfByte2 = a(paramLong);
		byte[] arrayOfByte1 = a(paramInt << 3 | 0x0);
		byte[] arrayOfByte3 = new byte[arrayOfByte2.length + arrayOfByte1.length];
		System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 0, arrayOfByte1.length);
		System.arraycopy(arrayOfByte2, 0, arrayOfByte3, arrayOfByte1.length, arrayOfByte2.length);
		return arrayOfByte3;
	}
	
	public static byte[] a(long j) {
        long abs = Math.abs(j);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        do {
            byteArrayOutputStream.write((byte) ((int) ((127 & abs) | 128)));
            abs >>= 7;
        } while (abs != 0);
        byte[] toByteArray = byteArrayOutputStream.toByteArray();
        int length = toByteArray.length - 1;
        toByteArray[length] = (byte) (toByteArray[length] & 127);
        return toByteArray;
    }
	
	protected static int GetNextSeq()
	{
		_seq++;
		// 为了兼容iQQ
		// iQQ把序列号的高位都为0，如果为1，它可能会拒绝，wqfox称是因为TX是这样做的
		int i = 0x7FFF;
		_seq = _seq & i;
		if (_seq == 0)
		{
			_seq++;
		}

		return _seq;
	}
}



