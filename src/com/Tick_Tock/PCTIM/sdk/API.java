package com.Tick_Tock.PCTIM.Sdk;
import com.Tick_Tock.PCTIM.Message.*;


public interface API {
	public void sendGroupMessage(String text,long groupUin);
	
	public void sendGroupImage(String text,long groupUin);
	
	public void sendGroupXml(String text,long groupUin);
	
	public void sendFriendMessage(String text,long groupUin);
	
	public void sendFriendXml(String text,long groupUin);
	
	public GroupList getGroupList();
	
	public FriendList getFriendList();
}
