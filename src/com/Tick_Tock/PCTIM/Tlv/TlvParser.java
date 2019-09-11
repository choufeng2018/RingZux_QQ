package com.Tick_Tock.PCTIM.Tlv;


import java.util.*;
import com.Tick_Tock.PCTIM.*;
import com.Tick_Tock.PCTIM.Utils.*;

public class TlvParser
{


	public static void parsetvl(Tlv tlv, QQUser user)
	{

		int tlvtype = tlv.tag;
		if (tlvtype == 274)
		{
			user.txprotocol.sigClientAddress = tlv.value;
		}
		else if (tlvtype == 23)
		{
			ByteReader bytefactory = new ByteReader(tlv.value);
			bytefactory.readBytes(1);
			byte[] WSubVer = bytefactory.readBytes(1);
			if (WSubVer[0] == 1)
            {
				
				long timeMillis = bytefactory.readInt();
                user.txprotocol.serverTime = (int) timeMillis;
                user.txprotocol.clientAdrres = Util.getIpStringFromBytes(bytefactory.readBytes(4));
                user.txprotocol.clientPort = (short) bytefactory.readShortAndDestroy();
			}
		}
		else if (tlvtype == 784)
		{
			user.txprotocol.serverAddres = Util.getIpStringFromBytes(tlv.value);
		}
		else if (tlvtype == 12)
		{
			ByteReader bytefactory = new ByteReader(tlv.value);
			bytefactory.readBytes(1);
			byte[] WSubVer = bytefactory.readBytes(1);
			if (WSubVer[0] == 2)
			{
				user.txprotocol.idc = bytefactory.readBytes(4); /*dwIDC =*/

				user.txprotocol.isp = bytefactory.readBytes(4); /*dwISP =*/
				bytefactory.readBytes(2);
				byte[] ip_byte = bytefactory.readBytes(4);
				user.txprotocol.redirectedAddress = Util.getIpStringFromBytes(ip_byte); /*dwRedirectIP =*/
				user.txprotocol.redirctionAdrresRecord.add(ip_byte);
				user.txprotocol.redirectedPort = (short) bytefactory.readShortAndDestroy();
				/*wRedirectPort =*/
			}
			else
			{
				System.out.println("未知版本类型 12");
			}

		}
		else if (tlvtype == 30)
		{
			user.txprotocol.tgtgtKey = tlv.value;
		}
		else if (tlvtype == 6)
		{
			user.txprotocol.tgtgt = tlv.value;
		}
		else if (tlvtype == 272)
		{
			ByteReader reader = new ByteReader(tlv.value);
			if (reader.readShort() != 1)
			{
			    System.out.println("未知版本 272");
			}
			else
			{
				user.txprotocol.sigImage = reader.readRestBytesAndDestroy();
			}
		}
		else if (tlvtype == 277)
		{
			byte[] bufPacketMD5 = tlv.value;
		}
		else if (tlvtype == 265)
		{
			ByteReader bytefactory = new ByteReader(tlv.value);
			bytefactory.readBytes(1);
			byte[] WSubVer = bytefactory.readBytes(1);
			if (WSubVer[0] == 1)
			{
				user.txprotocol.sessionKey = bytefactory.readBytes(16);

			    user.txprotocol.sigSession = bytefactory.readBytesByShortLength();

				user.txprotocol.pwdForConn = bytefactory.readBytesByShortLengthAndDestroy();
			}
			else
			{
				System.out.println("未知版本类型 265");
			}
		}
		else if (tlvtype == 259)
		{
			ByteReader bytefactory = new ByteReader(tlv.value);
			bytefactory.readBytes(1);
			byte[] WSubVer = bytefactory.readBytes(1);
			if (WSubVer[0] == 1)
			{

				user.txprotocol.sid = bytefactory.readBytesByShortLengthAndDestroy();
			}
			else
			{
				System.out.println("未知版本类型 259");
			}
		}
		else if (tlvtype == 263)
		{
			ByteReader bytefactory = new ByteReader(tlv.value);
			bytefactory.readBytes(1);
			byte[] WSubVer = bytefactory.readBytes(1);
			if (WSubVer[0] == 1)
			{
				bytefactory.readBytesByShortLength();
				user.txprotocol.tgtGtKey = bytefactory.readBytes(16);
				user.txprotocol.tgt = bytefactory.readBytesByShortLength();
				user.txprotocol._16BytesGtKeySt = bytefactory.readBytes(16);
				user.txprotocol.serviceTicket = bytefactory.readBytesByShortLength();
				byte[] http = bytefactory.readBytesByShortLengthAndDestroy();
				ByteReader httpfactory = new ByteReader(http);
				httpfactory.readBytes(1);
				user.txprotocol._16BytesGtKeyStHttp = httpfactory.readBytes(16);
				user.txprotocol.serviceTicketHttp = httpfactory.readBytesByShortLengthAndDestroy();
				//user.TXProtocol.BufGtKeyTgtPwd = httpfactory.readBytes(16);

			}
			else
			{
				System.out.println("未知版本类型 263");
			}

		}
		else if (tlvtype == 264)
		{
			ByteReader bytefactory = new ByteReader(tlv.value);
			bytefactory.readBytes(1);
			byte[] WSubVer = bytefactory.readBytes(1);
			if (WSubVer[0] == 1)
			{
				byte[] data = bytefactory.readBytesByShortLengthAndDestroy();
				ByteReader datafactory = new ByteReader(new ByteReader(data).readBytesByShortLengthAndDestroy());
				byte[] wSsoAccountWFaceIndex = datafactory.readBytes(2);
				int length = datafactory.readBytes(1)[0];
				if (length > 0)
				{
				    user.nickName = new String(datafactory.readString(length));
				}
				user.sex = datafactory.readBytes(1)[0];
				byte[] dwSsoAccountDwUinFlag = datafactory.readBytes(4);
                user.age = datafactory.readBytesAndDestroy(1)[0];
			}
			else
			{
				System.out.println("未知版本类型 264");
			}

		}
		else if (tlvtype == 13)
		{
			
		}
		else if (tlvtype == 31)
		{
			ByteReader bytefactory = new ByteReader(tlv.value);
			bytefactory.readBytes(1);
			byte[] WSubVer = bytefactory.readBytes(1);
			user.txprotocol.deviceId = bytefactory.readRestBytesAndDestroy();
		}
		else if (tlvtype == 20)
		{
			
		}
		else if (tlvtype == 268)
		{
			ByteReader bytefactory = new ByteReader(tlv.value);
			bytefactory.readBytes(1);
			byte[] WSubVer = bytefactory.readBytes(1);
			if (WSubVer[0] == 1)
            {

				user.txprotocol.sessionKey = bytefactory.readBytes(16);
				byte[] dwUin = bytefactory.readBytes(4);
				String dwClientIP = Util.getIpStringFromBytes(bytefactory.readBytes(4));
				user.txprotocol.clientPort = (short) bytefactory.readShortAndDestroy();

				//....
            }
            else
            {
                System.out.println("未知版本类型 268");
            }

		}
		else if (tlvtype == 270)
		{
			ByteReader bytefactory = new ByteReader(tlv.value);
			bytefactory.readBytes(1);
			byte[] WSubVer = bytefactory.readBytes(1);
			if (WSubVer[0] == 1)
            {
                ByteReader sigfactory = new ByteReader(bytefactory.readBytesByShortLengthAndDestroy());
				byte[] dwUinLevel =sigfactory.readBytes(3);
                byte[] dwUinLevelEx = sigfactory.readBytes(3);
                byte[] buf24ByteSignature = sigfactory.readBytesByShortLength();
                byte[] buf32ByteValueAddedSignature = sigfactory.readBytesByShortLength();
                byte[] buf12ByteUserBitmap = sigfactory.readBytesByShortLengthAndDestroy();
				user.txprotocol.clientKey = buf32ByteValueAddedSignature;
			}
            else
            {
                System.out.println("未知版本类型 270");
            }
		}
		else if (tlvtype == 47)
		{
			
		}
		else if (tlvtype == 269)
		{
			
		}
		else if (tlvtype == 261)
		{
			

		}
		else if (tlvtype == 256)
		{
			System.out.println(Util.byte2HexString(tlv.value));


		}


		else if (tlvtype == 260)
		{
			ByteReader factory = new ByteReader(tlv.value);

			int WSubVer = factory.readShort(); //wSubVer
            if (WSubVer == 0x0001)
            {
                int wCsCmd = factory.readShort();
                long errorCode = factory.readInt();

                factory.readBytes(1); //0x00
                factory.readBytes(1); //0x05
				byte PngData = factory.readBytes(1)[0]; //是否需要验证码：0不需要，1需要
                int len;
                if (PngData == 0x00)
                {
                    len = factory.readBytes(1)[0];
                    while (len == 0)
                    {
                        len = factory.readBytes(1)[0];
                    }
                }
                else //ReplyCode != 0x01按下面走 兼容多版本
                {
                    factory.readInt(); //需要验证码时为00 00 01 23，不需要时为全0
                    len = factory.readShort();
                }

                byte[] buffer = factory.readBytes(len);
                user.txprotocol.sigImage = buffer;
                if (PngData == 0x01) //有验证码数据
                {
                    len = factory.readShort();
                    buffer = factory.readBytes(len);
                    user.packet00BaVerifyCode = buffer;
                    user.next = factory.readBytes(1)[0];
                    factory.readBytes(1);
                    //var directory = Util.MapPath("Verify");
                    //var filename = Path.Combine(directory, user.QQ + ".png");
                    //if (!Directory.Exists(directory))
                    //{
                    //    Directory.CreateDirectory(directory);
                    //}

                    //var fs = Next == 0x00
                    //    ? new FileStream(filename, FileMode.Create, FileAccess.ReadWrite, FileShare.Read)
                    //    : new FileStream(filename, FileMode.Append, FileAccess.Write, FileShare.Read);

                    ////fs.Seek(0, SeekOrigin.End);
                    //fs.Write(buffer, 0, buffer.Length);
                    //fs.Close();
                    len = factory.readShort();
                    buffer = factory.readBytes(len);
                    user.txprotocol.verificationImageToken = buffer;
                    if (factory.hasMore())
                    {
                        factory.readShort();
                        len = factory.readShort();
                        buffer = factory.readBytes(len);
                        user.txprotocol.verificationImageKey = buffer;
                    }
					factory.destroy();
                }
            }

		}

		else
		{

			System.out.println("未知tlv解析:" + tlv.tag);
		}
	}
}
