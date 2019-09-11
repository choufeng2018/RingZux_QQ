package com.Tick_Tock.PCTIM;
import com.Tick_Tock.PCTIM.Message.*;
import com.Tick_Tock.PCTIM.Sdk.*;
import com.Tick_Tock.PCTIM.Utils.*;
import java.util.*;
public class QQUser
{
	public long uin;
	
	public byte[] md51;
	
	public byte[] md52;
	
	public String nickName;
	
	public int sex;
	
	public int age;
	
	public long loginTime;
	
	public boolean isLogined = false;
	
	public boolean offLine = false;
	
	public byte[] packet00BaVerifyCode=new byte[0];
	
	public byte next;
	
	public byte[] packet00BaKey = Util.RandomKey();
	
	public byte packet00BaSequence=0x01;
	
	public byte[] packet00BaFixKey =Util.str_to_byte("69 20 D1 14 74 F5 B3 93 E4 D5 02 B3 71 1A CD 2A");
	
	public String skey;
	
	public String bkn;
	
	public String pskey;
	
	public String qungtk;
	
	public String quncookie;
	
	public GroupList groupList;
	
	public FriendList friendList;
	
	public Ecdh ecdh;
	
	public List<PictureStore> imageStoreCache = new ArrayList<PictureStore>();
	
	public TXProtocol txprotocol  = new TXProtocol();

	public byte[] packet0825Key  = Util.RandomKey();

	public boolean isLoginRedirected;

	public byte[] packet0836Key1 = Util.RandomKey();
	
	public QQUser(long qq,byte[] pwd)
	{
		Ecdh rrr = new Ecdh();
		this.txprotocol.ecdhPublicKey= rrr.publickey;
		this.txprotocol.ecdhShareKey = rrr.sharekey;
		this.ecdh = rrr;
		uin = qq;
		SetPassword(pwd);
	}
	
	public void SetPassword(byte[] pwd)
	{
		md51 = pwd;
		md52 = Util.MD5(new ByteBuilder().writeBytes(md51).writeInt(0).writeInt(this.uin).getDataAndDestroy());
	}
}
