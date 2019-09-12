package com.Tick_Tock.PCTIM;
import com.Tick_Tock.PCTIM.Utils.*;
import java.util.*;

public class TXProtocol
{
	public byte[] verificationImageKey = new byte[0];
	
	public byte[] verificationImageToken =new byte[0];
	
	public byte[] clientVersion1 = {0x38};
	
	public byte[] clientVersion2 = {0x29};
	
	public byte[] fix1 = { 0x03, 0x00, 0x00 };
	
	public byte[] fix2 = { 0x00, 0x00, 0x00, 0x00 };
	
	public byte[] fix3 = {0x01};
	
	public byte[] ecdhProtocolVersion  = {0x00,0x02};//决定是否二次协商
	
	public byte[] ecdhAlgorithmVersion  = {0x01,0x03};
	
	public byte[] clientType = { 0x00, 0x01, 0x01, 0x01 };
	
	public byte[] fuckMe1 = { 0x00, 0x00, 0x69, 0x23 };
	
	public byte[] ssoVersion  = {0x00,0x00,0x04,0x56};
	
	public byte[] serviceId  = {0x00,0x00,0x00,0x01};
	
	public byte[] fuckMe2  = Util.str_to_byte("00 00 15 EB");
	
	public byte[] fuckMe3  = Util.str_to_byte("15 EB");
	
	public int  redirectionCount =0;
	
	public String serverAddres  = "";
	
	public short serverPort  = 0;
	
	public List<byte[]>  redirctionAdrresRecord = new ArrayList<byte[]>();
	
	public byte pingType  = 0x02;
	
	public byte[] ecdhPublicKey  ;
	
	public byte[] ecdhShareKey ;
	
	public String redirectedAddress ;
	
	public byte[] sigClientAddress ;
	
	public int serverTime ;
	
	public String clientAdrres ;
	
	public short clientPort ;
	
	public byte[] isp = new byte[] {0x00,0x00,0x00,0x00};
	
	public byte[] idc = new byte[] {0x00,0x00,0x12,0x00};
	
	public short redirectedPort ;
	
	public String computerName  = Util.readConfig("COMPUTER_NAME");
	
	public byte[] tgtgt ;
	
	public byte[] rememberPassword  = {0x00};
	
	public byte[] computerIdEx  = Util.random_byte(16);//{0x77,(byte)0x98,(byte)0x00,(byte)0x0B,(byte)0xAB,(byte)0x5D,(byte)0x4F,(byte)0x3D,(byte)0x30,(byte)0x50,(byte)0x65,(byte)0x2C,(byte)0x4A,(byte)0x2A,(byte)0xF8,(byte)0x65};
	
	public byte[] deviceId  = Util.random_byte(32);//{0x0f,(byte)0xab,(byte)0xbe,(byte)0x21,(byte)0x04,(byte)0xa7,(byte)0x2a,(byte)0xf1,(byte)0xe1,(byte)0x9d,(byte)0xa1,(byte)0x95,(byte)0x6a,(byte)0x36,(byte)0x3d,(byte)0xf0,(byte)0x7b,(byte)0x22,(byte)0xff,(byte)0x2e,(byte)0xc2,(byte)0xca,(byte)0xc9,(byte)0x2b,(byte)0xa8,(byte)0xd6,(byte)0xda,(byte)0x45,(byte)0x9d,(byte)0x31,(byte)0xa9,(byte)0x60};
	
	public byte[] computerId  =  Util.randomKey();//{ 0x43, 0x04, 0x21, 0x7D, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
	
	public byte[] tgtgtKey  = Util.randomKey();
	
	public byte[] sid  = {0x1E,(byte)0xC1,(byte)0x25,(byte)0x71,(byte)0xB2,(byte)0x4C,(byte)0xEA,(byte)0x91,(byte)0x9A,(byte)0x6E,(byte)0x8D,(byte)0xE6,(byte)0x95,(byte)0x4E,(byte)0xCE,(byte)0x06};
	
	public byte[] macGuid  = Util.randomKey();//{21,(byte)0x4B,(byte)0x1A,(byte)0x04,(byte)0x09,(byte)0xED,(byte)0x19,(byte)0x70,(byte)0x98,(byte)0x75,(byte)0x51,(byte)0xBB,(byte)0x2D,(byte)0x3A,(byte)0x7E,(byte)0x0A}
	
	public byte[] sigImage;
	
	public byte[] qdData ;
	
	public byte[] qdKey = { 0x77, 0x45, 0x37, 0x5e, 0x33, 0x69, 0x6d, 0x67, 0x23, 0x69, 0x29, 0x25, 0x68, 0x31, 0x32, 0x5d };
	
	public byte[] qdVerion = {0x02,0x04,0x04,0x04};
	
	public byte[] qdSufFix = { 0x68 };
	
	public byte[] qdPreFix = { 0x3E };
	
	public byte[] qdProtocolVersion = {0x00,0x63};
	
	public byte[] qdCsCmdNo = {0x00,0x04};
	
	public byte[] qdCcSubNo = {0x00};
	
	public byte[] osType = {0x03};
	
	public byte[] isWow64 = {0x01};
	
	public byte[] drvVersionInfo = {0x01,0x02};
	
	public byte[] versionTsSafeEditDat = {07,(byte)0xdf,(byte)0x00,(byte)0x0a,(byte)0x00,(byte)0x0c,(byte)0x00,(byte)0x01};
	
	public byte[] versionQScanEngineDll = { 0x00, 0x04, 0x00, 0x03, 0x00, 0x04, 0x20, 0x5c };
	
	public byte[] tgtGtKey ;
	
	public byte[] tgt ;
	
	public byte[] _16BytesGtKeySt ;
	
	public byte[] serviceTicket ;
	
	public byte[] _16BytesGtKeyStHttp ;
	
	public byte[] serviceTicketHttp ;
	
	public byte[] gtKeyTgtPwd ;
	
	public byte[] sessionKey ;
	
	public byte[] sigSession ;
	
	public byte[] pwdForConn ;
	
	public byte[] clientKey ;
	
	public byte[] qqexeMD5  = {(byte)0xfa,(byte)0xcf,(byte)0x7c,(byte)0xc5,(byte)0xae,(byte)0x02,(byte)0xe6,(byte)0x65,(byte)0x0c,(byte)0x01,(byte)0x07,(byte)0xcd,(byte)0xfe,(byte)0x0e,(byte)0x1b,(byte)0x2c};
	
	public byte[] Online = {0x0A};
}
