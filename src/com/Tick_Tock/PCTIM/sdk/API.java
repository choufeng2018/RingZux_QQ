package com.Tick_Tock.PCTIM.sdk;
import com.Tick_Tock.PCTIM.Message.*;


public interface API {
	public void SendGroupMessage(String text,long groupUin);
	
	public void SendGroupImage(String text,long groupUin);
	
	public void SendGroupXml(String text,long groupUin);
	
	public void SendFriendMessage(String text,long groupUin);
	
	public void SendFriendXml(String text,long groupUin);
	
	public Group_List getgrouplist();
	
	public Friend_List getfriendlist();
}
