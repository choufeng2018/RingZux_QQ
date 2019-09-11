package com.Tick_Tock.PCTIM.Utils;

import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import javax.crypto.KeyAgreement;
import java.security.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.ECPointUtil;
import java.security.spec.ECField;
import java.security.spec.ECFieldFp;
import java.security.spec.EllipticCurve;
import java.security.KeyPair;
import java.security.Security;
import java.security.KeyFactory;

public class Ecdh
{
	//公钥
	public  byte[] publickey;

	//密钥
	public byte[] sharekey;

	//椭圆曲线
	private ECParameterSpec ecSpec;

	//应该是本地密钥对
	private KeyPair pair;

	//这东西啥玩意我也不知道
	private final static byte[] S_pub_key =new byte[] { 0x04, (byte)0xBF, (byte)0x47, (byte)0xA1, (byte)0xCF, (byte)0x78, (byte)0xA6, (byte)0x29, (byte)0x66, (byte)0x8B,
		0x0B, (byte)0xC3, (byte)0x9F, (byte)0x8E, (byte)0x54, (byte)0xC9, (byte)0xCC, (byte)0xF3, (byte)0xB6, (byte)0x38,
		0x4B, (byte)0x08, (byte)0xB8, (byte)0xAE, (byte)0xEC, (byte)0x87, (byte)0xDA, (byte)0x9F, (byte)0x30, (byte)0x48,
		0x5E, (byte)0xDF, (byte)0xE7, (byte)0x67, (byte)0x96, (byte)0x9D, (byte)0xC1, (byte)0xA3, (byte)0xAF, (byte)0x11,
		0x15, (byte)0xFE, (byte)0x0D, (byte)0xCC, (byte)0x8E, (byte)0x0B, (byte)0x17, (byte)0xCA, (byte)0xCF
	};
	public Ecdh()
	{
		//不知是否有必要，不过还是加上去吧
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		//生成711椭圆曲线
		this.ecSpec  = secp192k1(18, 1,
								 "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFEE37",
								 "000000000000000000000000000000000000000000000000",
								 "000000000000000000000000000000000000000000000003",
								 "DB4FF10EC057E9AE26B07D0280B7F4341DA5D1B1EAE06C7D",
								 "9B2F2F6D9C5628A7844163D015BE86344082AA88D95E2F9D",
								 "FFFFFFFFFFFFFFFFFFFFFFFE26F2FC170F69466A74DEFD8D",
								 1);
		try
		{
			System.out.println("生成密匙");
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDH", "BC");
			keyGen.initialize(ecSpec);
			this.pair = keyGen.generateKeyPair();
			BCECPublicKey cpk = (BCECPublicKey) pair.getPublic();
			ECPoint point =  cpk.getQ();
			byte[] ecdhkey = point.getEncoded(true);//本地公钥
			System.out.println("一次协商");
			java.security.spec.ECPoint sp = ECPointUtil.decodePoint(ecSpec.getCurve(), S_pub_key);
			KeyFactory kf = KeyFactory.getInstance("ECDH", "BC");
			ECPublicKeySpec pubSpec = new ECPublicKeySpec(sp, ecSpec);
			ECPublicKey myECPublicKey = (ECPublicKey) kf.generatePublic(pubSpec);
			KeyAgreement agreement = KeyAgreement.getInstance("ECDH", "BC");
			agreement.init(pair.getPrivate());
			agreement.doPhase(myECPublicKey, true);
			byte[] xx = agreement.generateSecret();
			this.publickey = ecdhkey;
			//sharekey不止16位，pcqq需要截取前16位而安卓qq则是md5下
			this.sharekey = new ByteReader(xx).readBytesAndDestroy(16);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	//二次协商直接返回sharekey
	public byte[] consult(byte[] data)
	{
		System.out.println("二次协商");
		java.security.spec.ECPoint sp = ECPointUtil.decodePoint(this.ecSpec.getCurve(), data);
		try
		{
			KeyFactory kf = KeyFactory.getInstance("ECDH", "BC");
			ECPublicKeySpec pubSpec = new ECPublicKeySpec(sp, this.ecSpec);
			ECPublicKey myECPublicKey = (ECPublicKey) kf.generatePublic(pubSpec);
			KeyAgreement agreement = KeyAgreement.getInstance("ECDH", "BC");
			agreement.init(this.pair.getPrivate());
			agreement.doPhase(myECPublicKey, true);
			byte[] xx = agreement.generateSecret();
			return new ByteReader(xx).readBytesAndDestroy(16);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static BigInteger bi(String s)
	{
		return new BigInteger(s, 16);
	}

	private static ECParameterSpec secp192k1(int code, int type, String p, String a, String b, String x, String y,String n, int h)
	{
		BigInteger bip = bi(p);
		ECField field = new ECFieldFp(bip);
		EllipticCurve curve = new EllipticCurve(field, bi(a), bi(b));
		java.security.spec.ECPoint g = new java.security.spec.ECPoint(bi(x), bi(y));
		return new ECParameterSpec(curve, g, bi(n), h);
	}
}

