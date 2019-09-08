package com.Tick_Tock.PCTIM.TLV;
import java.util.*;
import com.Tick_Tock.PCTIM.Utils.*;
import com.Tick_Tock.PCTIM.*;

public class TLVFactory
{
	public static byte[] tlv0018(QQUser user){
		ByteBuilder builder = new ByteBuilder();
	    byte[] WSubVer ={0x00,0x01};
		builder.writeBytes(WSubVer); //wSubVer 
		builder.writeBytes(user.TXProtocol.DwSsoVersion); //dwSSOVersion
		builder.writeBytes(user.TXProtocol.DwServiceId); //dwServiceId
		builder.writeBytes(user.TXProtocol.DwClientVer); //dwClientVer
		builder.writeInt(user.QQ);
		builder.writeShort(user.TXProtocol.WRedirectCount); //wRedirectCount 
		builder.writeBytes(new byte[]{00,00});
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x00,0x18});//头部
		
		return builder.getDataAndDestroy();
	}
	
	public static byte[] tlv0309(QQUser user){
		ByteBuilder builder = new ByteBuilder();
	    byte[] WSubVer ={0x00,0x01};
		builder.writeBytes(WSubVer); //wSubVer
		builder.writeBytes(Util.IPStringToByteArray(user.TXProtocol.DwServerIP)); //LastServerIP - 服务器最后的登录IP，可以为0
		builder.writeBytes( Util.subByte(Util.ToByte(user.TXProtocol.WRedirectips.size()),3,1)); //cRedirectCount - 重定向的次数（IP的数量）
		for (byte[] ip : user.TXProtocol.WRedirectips)
			{
				builder.writeBytes(ip);
			}

		builder.writeBytes(user.TXProtocol.CPingType); //cPingType 
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x03,0x09});//头部

		return builder.getDataAndDestroy();
	}
	
	public static byte[] tlv0036(int ver){
		ByteBuilder builder = new ByteBuilder();
		
		if (ver ==2){
			builder.writeBytes(new byte[]{0x00,0x02,0x0,0x01,0x00,0x00,0x00,0x05,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00}); //wSubVer
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
	    byte[] WSubVer ={0x01,0x02};
		builder.rewriteBytes(WSubVer); //wDHVer
		builder.writeBytesByShortLength(user.TXProtocol.BufDhPublicKey); //bufDHPublicKey长度
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x01,0x14});//头部

		return builder.getDataAndDestroy();
	}
	
	
	
	public static byte[] tlv0112(QQUser user){
	    
		ByteBuilder builder = new ByteBuilder();
		
		builder.writeBytes(user.TXProtocol.BufSigClientAddr);
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x01,0x12});//头部

		return builder.getDataAndDestroy();
	}
	
	public static byte[] tlv030f(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		builder.writeBytesByShortLength(user.TXProtocol.BufComputerName.getBytes());
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x03,0x0f});//头部

		return builder.getDataAndDestroy();
	}
	
	public static byte[] tlv0005(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		byte[] WSubVer = {0x00,0x02};
		builder.writeBytes(WSubVer);
		builder.writeInt(user.QQ);
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x00,0x05});//头部
		return builder.getDataAndDestroy();
	}
	
	
	public static byte[] tlv0006(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		if (user.TXProtocol.BufTgtgt == null){
			byte[] WSubVer = {0x00,0x02};
			byte[] random_byte = Util.random_byte(4);
			builder.writeBytes(random_byte);
			builder.writeBytes(WSubVer);
			builder.writeInt(user.QQ);
			builder.writeBytes(user.TXProtocol.DwSsoVersion);
			builder.writeBytes(user.TXProtocol.DwServiceId);
			builder.writeBytes(user.TXProtocol.DwClientVer);
			builder.writeBytes(new byte[]{0x00,0x00});
			builder.writeBytes(user.TXProtocol.BRememberPwdLogin);
			builder.writeBytes(user.MD51);    //密码
			builder.writeBytes(user.TXProtocol.DwServerTime_Byte);
			builder.writeBytes(new byte[13]);
			builder.writeBytes(user.TXProtocol.DwClientIP_Byte);
			builder.writeBytes(user.TXProtocol.DwIsp); //dwISP
			builder.writeBytes(user.TXProtocol.DwIdc); //dwIDC
			builder.writeBytesByShortLength(user.TXProtocol.BufComputerIdEx); //机器码
			builder.writeBytes(user.TXProtocol.BufTgtgtKey); //临时密匙
			Crypter crypter = new Crypter();
			user.TXProtocol.BufTgtgt = crypter.encrypt(builder.getDataAndDestroy(), user.MD52);
		}
		builder.clean();
		builder.writeBytes(user.TXProtocol.BufTgtgt);
		
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
		builder.writeBytes(user.TXProtocol.BufComputerId_crc32_reversed);
		builder.writeBytesByShortLength(user.TXProtocol.BufComputerId);
		builder.writeByte((byte)0x02);
		builder.writeBytes(user.TXProtocol.BufComputerIdEx_crc32_reversed);
		
		builder.writeBytesByShortLength(user.TXProtocol.BufComputerIdEx);
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x00,0x15});//头部

		return builder.getDataAndDestroy();
	}


	public static byte[] tlv001a(byte[] tlv0015, QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		
		Crypter crypter = new Crypter();
		
		builder.writeBytes(crypter.encrypt(tlv0015,user.TXProtocol.BufTgtgtKey));
		
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x00,0x1a});//头部

		return builder.getDataAndDestroy();
	}

	
	public static byte[] tlv0103(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		byte[] WSubVer = {0x00,0x01};
		builder.writeBytes(WSubVer);
		builder.writeBytesByShortLength(user.TXProtocol.BufSid);
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
		
		builder.writeBytesByShortLength(user.TXProtocol.BufMacGuid);
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
		if (user.TXProtocol.BufSigPic == null){
		    pic_byte = Util.RandomKey(56);
		}else{
			pic_byte = user.TXProtocol.BufSigPic;
		}
		builder.writeBytesByShortLength(pic_byte);
		byte[] crckey = Util.RandomKey(16);
		byte[] crccode = Util.get_crc32(crckey);
		
		builder.writeBytesByShortLength(Util.byteMerger(crckey,crccode));
		
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x01,0x02});//头部

		return builder.getDataAndDestroy();
	}

	public static byte[] tlv0110(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		byte[] WSubVer = {0x00,0x01};
		if (user.TXProtocol.BufSigPic == null)
		{
			return new byte[] { };
		}else{
			
			builder.writeBytes(WSubVer); //wSubVer
			builder.writeBytesByShortLength(user.TXProtocol.BufSigPic);
			builder.rewriteShort(builder.totalcount()); //长度
			builder.rewriteBytes(new byte[]{0x01,0x10});//头部
			return builder.getDataAndDestroy();
		}
		
	}
	
	public static byte[] tlv0032(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		builder.writeBytes(Util.GetQdData(user));
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x00,0x32});//头部

		return builder.getDataAndDestroy();
	}
	
	public static byte[] tlv0007(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		builder.writeBytes(user.TXProtocol.BufTgt);
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
		builder.writeBytes(user.TXProtocol.DwIdc);
		builder.writeBytes(user.TXProtocol.DwIsp);
		if (user.TXProtocol.DwServerIP == null){
		    builder.writeBytes(Util.IPStringToByteArray(user.TXProtocol.DwServerIP));
		}else{
			builder.writeBytes(Util.IPStringToByteArray(user.TXProtocol.DwRedirectIP));
			
		}
		builder.writeInt(user.TXProtocol.WServerPort);
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
		builder.writeBytes(user.TXProtocol.BufDeviceId);
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x00,0x1f});//头部

		return builder.getDataAndDestroy();
	}
	
	public static byte[] tlv0105(QQUser user)
	{
		ByteBuilder builder = new ByteBuilder();
		
		byte[] WSubVer = {0x00,0x01};
		builder.writeBytes(WSubVer);
		builder.writeBytes(user.TXProtocol.XxooB);
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
		byte[] newbyte = user.TXProtocol.BufTgt;
		byte flag = EncodeLoginFlag(newbyte, QQGlobal.QqexeMD5);
		builder.writeBytes(user.MD51);
		builder.writeByte(flag);
		builder.writeByte((byte)0x10);
		builder.writeBytes(new byte[]{0x00,0x00,0x00,0x00});
		builder.writeBytes(new byte[]{0x00,0x00,0x00,0x02});
		byte[] qddata = Util.GetQdData(user);
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
		builder.writeBytes(Util.IPStringToByteArray(Util.getHostIP()));
		builder.rewriteShort(builder.totalcount()); //长度
		builder.rewriteBytes(new byte[]{0x00,0x2d});//头部

		return builder.getDataAndDestroy();
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

