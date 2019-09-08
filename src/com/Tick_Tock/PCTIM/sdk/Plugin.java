package com.Tick_Tock.PCTIM.sdk;

import com.Tick_Tock.PCTIM.Message.*;

	public interface Plugin {
	    String author();
		String version();
		String name();
		
		void onLoad(API api);
		void onMessageHandler(QQMessage message);
	}

