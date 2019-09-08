package com.Tick_Tock.PCTIM;
import com.Tick_Tock.PCTIM.Client.*;
import com.Tick_Tock.PCTIM.Utils.*;
import java.util.concurrent.*;
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
		RingzuxClient app = new RingzuxClient(443, Util.http_dns("tcpconn3.tencent.com"));
		new Thread(app).start();
	}
}


