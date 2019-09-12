package com.Tick_Tock.PCTIM;
import com.Tick_Tock.PCTIM.Client.*;
import com.Tick_Tock.PCTIM.Utils.*;
import javax.net.ssl.*;

public class Main
{
	public static void main(final String[] args)
	{
		try
		{
			Util.trustAllHttpsCertificates();
			HttpsURLConnection.setDefaultHostnameVerifier(Util.hv);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
//		byte[] j = Util.str_to_byte("89 50 4E 47 0D 0A 1A 0A 00 00 00 0D 49 48 44 52 00 00 00 94 00 00 00 94 01 00 00 00 00 5D 47 3D 79 00 00 00 09 70 48 59 73 00 00 0B 13 00 00 0B 13 01 00 9A 9C 18 00 00 01 5D 49 44 41 54 48 89 AD 96 DB 91 C3 30 0C 03 D9 81 FA EF 12 1D F0 B8 A0 92 FB 17 E2 8C 15 6A 9D 71 28 F0 21 55 55 9D 9E DB 83 0E D3 4A D8 E9 B6 31 DF 9A D1 D3 88 61 54 0F 99 7F 39 80 9C F5 38 DB 20 1E FF 80 09 7F 4B 17 84 6C 16 DD 07 35 1B 05 BE BA BC 32 EB F9 7F 7D E3 F6 CA 9A 4B 72 BC 91 62 AE 88 A9 08 8E DA A2 96 7F 15 31 40 59 55 EB 31 E2 66 6C E2 5D 63 E8 86 4A 33 46 EC 48 82 CB D6 DC 95 B1 31 CB B9 8D 1E 2C 80 3A 8C 58 D9 73 6B 2A E7 68 C4 78 FB CC 58 3D 98 DC CC 98 9C 34 C8 C0 8C 79 C4 B0 57 DB B6 D7 6B BF 33 B2 FC F6 2F 62 3F 8F 4E C4 E4 26 EB A0 E3 BB 35 4D 18 A9 E8 5A 51 DB 24 E6 09 63 E1 DB 0F DD 30 A8 C5 8C 8D BF C8 49 D4 B9 1D B7 80 91 91 F2 C7 EF 66 88 98 DC 64 0E E1 29 E9 06 3E 60 44 BD DC C4 56 5D 6F 2B 01 73 A3 C1 D7 E9 0F DE 99 95 32 77 19 D9 77 F2 11 4D 02 C6 F2 BD 55 69 FB F6 09 99 C5 6C F9 0C 82 06 9F DA 79 66 BD 41 96 EE 8E 10 B2 5B 82 23 AD 13 1E 6D 23 76 0F 45 1C 1A 7A 77 AC 90 51 D5 DA 73 83 FF F2 07 8C 8E 43 AC DC B7 51 22 64 8E 0E 0F A4 35 13 46 21 7B BA 7B A7 6B 30 60 E5 A3 AF 3B 22 87 9A FE 9C 9B 1E D9 1F 2B 9E FC A5 55 4E D9 88 00 00 00 00 49 45 4E 44 AE 42 60 82 03 06 00 06 00 00 00 78 00 02");
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		Util.displayQrcode(j);
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
		RingzuxClient app = new RingzuxClient(443, Util.readConfig("SEVER_ADRRES"));
		new Thread(app).start();
		
	}
}


