package com.Tick_Tock.PCTIM.Tlv;
import java.util.*;
import com.Tick_Tock.PCTIM.Utils.*;
import com.Tick_Tock.PCTIM.*;

public class TlvBuiler
{
	public static byte[] tlv0018(QQUser user){
		ByteBuilder builder = new ByteBuilder();
	    byte[] WSubVer ={0x00,0x01};
		builder.writeBytes(WSubVer); //wSubVer 
		builder.writeBytes(user.txprotocol.ssoVersion); //dwSSOVersion
		builder.writeBytes(user.txprotocol.serviceId); //dwServiceId
		builder.writeBytes(user.txprotocol.fuckMe2); //dwClientVer
		builder.writeInt(user.uin);
		builder.writeShort(user.txprotocol.redirectionCount); //wRedirectCount 
		builder.writeBytes(new byte[]{00,00});
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x00,0x18});//头部
		
		return builder.getDataAndDestroy();
	}
	
	public static byte[] tlv0309(QQUser user){
		ByteBuilder builder = new ByteBuilder();
	    byte[] WSubVer ={0x00,0x01};
		builder.writeBytes(WSubVer); //wSubVer
		builder.writeBytes(Util.iPStringToByteArray(user.txprotocol.serverAddres)); //LastServerIP - 服务器最后的登录IP，可以为0
		builder.writeByte((byte)user.txprotocol.redirctionAdrresRecord.size()); //cRedirectCount - 重定向的次数（IP的数量）
		for (byte[] ip : user.txprotocol.redirctionAdrresRecord)
			{
				builder.writeBytes(ip);
			}

		builder.writeByte(user.txprotocol.pingType); //cPingType 
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x03,0x09});//头部

		return builder.getDataAndDestroy();
	}
	
	public static byte[] tlv0036(int ver){
		ByteBuilder builder = new ByteBuilder();
		
		if (ver ==2){
			builder.writeBytes(new byte[]{0x00,0x02,0x0,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00}); //wSubVer
			builder.rewriteShort(builder.totalcount()); //长度
			builder.rewriteBytes(new byte[]{0x00,0x36});//头部
		}else if  (ver ==1){
			builder.rewriteBytes(new byte[]{0x00,0x01,0x0,0x01,0x0,0x00,0x0,0x00}); //wSubVer
			builder.rewriteShort(builder.totalcount()); //长度
			builder.rewriteBytes(new byte[]{0x00,0x36});//头部
		}
		
		return builder.getDataAndDestroy();
	}
	
	
	public static byte[] tlv0114(QQUser user){
		ByteBuilder builder = new ByteBuilder();
	    byte[] WSubVer ={0x01,0x03};
		builder.rewriteBytes(WSubVer); //wDHVer
		builder.writeBytesByShortLength(user.txprotocol.ecdhPublicKey); //bufDHPublicKey长度
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x01,0x14});//头部

		return builder.getDataAndDestroy();
	}
	
	
	
	public static byte[] tlv0112(QQUser user){
	    
		ByteBuilder builder = new ByteBuilder();
		
		builder.writeBytes(user.txprotocol.sigClientAddress);
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x01,0x12});//头部

		return builder.getDataAndDestroy();
	}
	
	public static byte[] tlv030f(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		builder.writeBytesByShortLength(user.txprotocol.computerName.getBytes());
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x03,0x0f});//头部

		return builder.getDataAndDestroy();
	}
	
	public static byte[] tlv0005(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		byte[] WSubVer = {0x00,0x02};
		builder.writeBytes(WSubVer);
		builder.writeInt(user.uin);
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x00,0x05});//头部
		return builder.getDataAndDestroy();
	}
	
	
	public static byte[] tlv0006(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		if (user.txprotocol.tgtgt == null){
			byte[] WSubVer = {0x00,0x02};
			byte[] random_byte = Util.random_byte(4);
			builder.writeBytes(random_byte);
			builder.writeBytes(WSubVer);
			builder.writeInt(user.uin);
			builder.writeBytes(user.txprotocol.ssoVersion);
			builder.writeBytes(user.txprotocol.serviceId);
			builder.writeBytes(user.txprotocol.fuckMe2);
			builder.writeBytes(new byte[]{0x00,0x00});
			builder.writeBytes(user.txprotocol.rememberPassword);
			builder.writeBytes(user.md51);    //密码
			builder.writeInt(user.txprotocol.serverTime);
			builder.writeBytes(new byte[13]);
			builder.writeBytes(Util.iPStringToByteArray(user.txprotocol.clientAdrres));
			builder.writeInt(0); //dwISP
			builder.writeInt(0); //dwIDC
			builder.writeBytesByShortLength(user.txprotocol.computerIdEx); //机器码
			builder.writeBytes(user.txprotocol.tgtgtKey); //临时密匙
			TeaCryptor crypter = new TeaCryptor();
			user.txprotocol.tgtgt = crypter.encrypt(builder.getDataAndDontDestroy(), user.md52);
		}
		builder.clean();
		builder.writeBytes(user.txprotocol.tgtgt);
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x00,0x06});//头部
		return builder.getDataAndDestroy();
	}

	
	public static byte[] tlv0015(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		byte[] WSubVer = {0x00,0x01};
		builder.writeBytes(WSubVer);
		builder.writeByte((byte)0x01);
		builder.writeBytes(Util.get_crc32(user.txprotocol.computerId));
		builder.writeBytesByShortLength(user.txprotocol.computerId);
		builder.writeByte((byte)0x02);
		builder.writeBytes(Util.get_crc32(user.txprotocol.computerIdEx));
		builder.writeBytesByShortLength(user.txprotocol.computerIdEx);
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x00,0x15});//头部
		return builder.getDataAndDestroy();
	}


	public static byte[] tlv001a(byte[] tlv0015, QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		TeaCryptor crypter = new TeaCryptor();
		builder.writeBytes(crypter.encrypt(tlv0015,user.txprotocol.tgtgtKey));
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x00,0x1a});//头部

		return builder.getDataAndDestroy();
	}

	
	public static byte[] tlv0103(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		byte[] WSubVer = {0x00,0x01};
		builder.writeBytes(WSubVer);
		builder.writeBytesByShortLength(user.txprotocol.sid);
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x01,0x03});//头部

		return builder.getDataAndDestroy();
		
	}

	public static byte[] tlv0312()
	{
		ByteBuilder builder = new ByteBuilder();
	
		builder.writeBytes(new byte[]{0x01,0x00,0x00,0x00,0x01});
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x03,0x12});//头部

		return builder.getDataAndDestroy();
	}

	public static byte[] tlv0508()
	{
		ByteBuilder builder = new ByteBuilder();

		builder.writeBytes(new byte[]{0x01,0x00,0x00,0x00,0x00});
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x05,0x08});//头部

		return builder.getDataAndDestroy();
	}
	
	public static byte[] tlv0313(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();

		builder.writeBytes(new byte[]{0x01,0x01,0x02});
		
		builder.writeBytesByShortLength(user.txprotocol.macGuid);
		builder.writeBytes(new byte[]{0x00,0x00,0x00,0x02});
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x03,0x13});//头部

		return builder.getDataAndDestroy();
	}
	
	public static byte[] tlv0102(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		byte[] WSubVer = {0x00,0x01};
		builder.writeBytes(WSubVer);
		
		builder.writeBytes(Util.str_to_byte("9e9b03236d7fa881a81072ec5097968e"));
		byte[] pic_byte = null;
		if (user.txprotocol.sigImage == null){
		    pic_byte = Util.RandomKey(56);
		}else{
			pic_byte = user.txprotocol.sigImage;
		}
		builder.writeBytesByShortLength(pic_byte);
		byte[] crckey = Util.RandomKey(16);
		byte[] crccode = Util.get_crc32(crckey);
		builder.writeShort(crccode.length+16);
		builder.writeBytes(crckey);
		builder.writeBytes(crccode);
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x01,0x02});//头部

		return builder.getDataAndDestroy();
	}

	public static byte[] tlv0110(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		byte[] WSubVer = {0x00,0x01};
		if (user.txprotocol.sigImage == null)
		{
			return new byte[] { };
		}else{
			
			builder.writeBytes(WSubVer); //wSubVer
			builder.writeBytesByShortLength(user.txprotocol.sigImage);
			builder.rewriteShort(builder.totalcount()); //长度
			builder.rewriteBytes(new byte[]{0x01,0x10});//头部
			return builder.getDataAndDestroy();
		}
		
	}
	
	public static byte[] tlv0032(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		builder.writeBytes(GetQdData(user));
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x00,0x32});//头部

		return builder.getDataAndDestroy();
	}
	
	public static byte[] tlv0007(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		builder.writeBytes(user.txprotocol.tgt);
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x00,0x07});//头部

		return builder.getDataAndDestroy();
	}

	public static byte[] tlv000c(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		byte[] WSubVer = {0x00,0x02};
		builder.writeBytes(WSubVer);
		builder.writeBytes(new byte[]{0x00,0x00});
		builder.writeBytes(user.txprotocol.idc);
		builder.writeBytes(user.txprotocol.isp);
		if (user.txprotocol.serverAddres == null){
		    builder.writeBytes(Util.iPStringToByteArray(user.txprotocol.serverAddres));
		}else{
			builder.writeBytes(Util.iPStringToByteArray(user.txprotocol.redirectedAddress));
			
		}
		builder.writeInt(user.txprotocol.serverPort);
		builder.writeBytes(new byte[]{0x00,0x00});
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x00,0x0c});//头部

		return builder.getDataAndDestroy();
	}

	public static byte[] tlv001f(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		byte[] WSubVer = {0x00,0x01};
		builder.writeBytes(WSubVer);
		builder.writeBytes(user.txprotocol.deviceId);
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x00,0x1f});//头部

		return builder.getDataAndDestroy();
	}
	
	public static byte[] tlv0105(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		
		byte[] WSubVer = {0x00,0x01};
		builder.writeBytes(WSubVer);
		builder.writeBytes(user.txprotocol.fix3);
		builder.writeBytes(new byte[]{0x02,0x00,0x14,0x01,0x01,0x00,0x10});
		builder.writeBytes(Util.RandomKey());
		builder.writeBytes(new byte[]{0x00,0x14,0x01,0x02,0x00,0x10});
		builder.writeBytes(Util.RandomKey());
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x01,0x05});//头部

		return builder.getDataAndDestroy();
	}

	public static byte[] tlv010b(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();

		byte[] WSubVer = {0x00,0x02};
		builder.writeBytes(WSubVer);
		byte[] newbyte = user.txprotocol.tgt;
		byte flag = EncodeLoginFlag(newbyte, user.txprotocol.qqexeMD5);
		builder.writeBytes(user.md51);
		builder.writeByte(flag);
		builder.writeByte((byte)0x10);
		builder.writeBytes(new byte[]{0x00,0x00,0x00,0x00});
		builder.writeBytes(new byte[]{0x00,0x00,0x00,0x02});
		byte[] qddata = GetQdData(user);
		builder.writeBytesByShortLength(qddata);
		builder.writeBytes(new byte[]{0x00,0x00,0x00,0x00});
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x01,0x0b});//头部

		return builder.getDataAndDestroy();
	}

	public static byte[] tlv002d(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		byte[] WSubVer = {0x00,0x01};
		builder.writeBytes(WSubVer);
		builder.writeBytes(Util.iPStringToByteArray(Util.getHostIP()));
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x00,0x2d});//头部

		return builder.getDataAndDestroy();
	}

	public static byte[] GetQdData(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		builder.writeBytes(Util.iPStringToByteArray(user.txprotocol.serverAddres));
		ByteBuilder builder2 = new ByteBuilder();
		builder2.writeBytes(user.txprotocol.qdVerion);
		builder2 .writeBytes(new byte[]{0x00,0x00,0x00,0x00});
		builder2.writeBytes( user.txprotocol.fuckMe1);
		builder2.writeInt(user.uin);
		builder2.writeShort(builder.totalcount());

		builder.clean();
		builder.writeBytes(user.txprotocol.qdPreFix);
		builder .writeBytes(user.txprotocol.qdProtocolVersion);
		builder.writeBytes(user.txprotocol.qdVerion);
		builder .writeBytes(new byte[]{0x00});
		builder.writeBytes(user.txprotocol.qdCsCmdNo);
		builder.writeBytes(user.txprotocol.qdCcSubNo);
		builder .writeBytes( Util.str_to_byte("0E88"));
		builder.writeBytes(new byte[]{0x00,0x00,0x00,0x00});
		builder.writeBytes(user.txprotocol.computerIdEx);
		builder.writeBytes(user.txprotocol.osType);
		builder .writeBytes(user.txprotocol.isWow64);
		builder.writeBytes(user.txprotocol.fuckMe1);
		builder .writeBytes(user.txprotocol.fuckMe2);
		builder .writeBytes( new byte[]{0x00,0x00});
		builder .writeBytes( user.txprotocol.drvVersionInfo);
		builder .writeBytes( new byte[]{0x00,0x00,0x00,0x00});
		builder  .writeBytes( user.txprotocol.versionTsSafeEditDat);
		builder  .writeBytes(user.txprotocol.versionQScanEngineDll);
		builder .writeBytes(new byte[]{0x00});
		TeaCryptor crypter = new TeaCryptor();
		builder  .writeBytes( crypter.encrypt(builder2.getDataAndDontDestroy(), user.txprotocol.qdKey));
		builder .writeBytes( user.txprotocol.qdSufFix);
		int size = builder.totalcount() + 3;
		builder2.clean();
		builder2 .writeBytes(user.txprotocol.qdPreFix);
		builder2 .writeInt(size);
		builder2 .writeBytes(new byte[]{0x00,0x00});
		builder2  .writeBytes(new byte[]{0x00,0x00});
		builder2  .writeInt(builder.totalcount());
		builder2  .writeBytes(new byte[]{0x00,0x00});
		builder2 .writeBytes( new byte[]{0x00,0x00});
		user.txprotocol.qdData = builder.getDataAndDestroy();
		builder2.destroy();
		return user.txprotocol.qdData;
	}
	
	
	
	private static byte EncodeLoginFlag(byte[] bufTgt /*bufTGT*/, byte[] qqexeMD5 /*QQEXE_MD5*/)
	{
		byte flag = 1;
		byte rc = flag;
		for (byte t : bufTgt)
		{
			rc ^= t;
		}

		for (int i = 0; i < 4; i++)
		{
			int rcc = qqexeMD5[i * 4]&0x0ffffff;
			rcc ^= qqexeMD5[i * 4 + 1];
			rcc ^= qqexeMD5[i * 4 + 3];
			rcc ^= qqexeMD5[i * 4 + 2];
			rc ^= rcc;
		}
		return rc;
	}
	
	
}

