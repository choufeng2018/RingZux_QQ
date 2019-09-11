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
		RingzuxClient app = new RingzuxClient(443, Util.readConfig("SEVER_ADRRES"));
		new Thread(app).start();
	}
}


