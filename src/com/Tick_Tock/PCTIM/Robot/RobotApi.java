package com.Tick_Tock.PCTIM.Robot;
import com.Tick_Tock.PCTIM.Sdk.*;

import com.Tick_Tock.PCTIM.*;
import com.Tick_Tock.PCTIM.Message.*;
import com.Tick_Tock.PCTIM.Utils.*;
import com.Tick_Tock.PCTIM.Client.*;

public class RobotApi implements API
{
	@Override public void sendFriendXml(String text, long friendUin)
	{
		SendMessage.sendFriendXmlMessage(this.user, this.socket, text,friendUin);
	}

	@Override public void sendGroupMessage(String text, long groupUin)
	{
		SendMessage.sendGroupMessage(this.user, this.socket, text,groupUin);
	}

	@Override public void sendGroupImage(String text, long groupUin)
	{
		SendMessage.sendGroupImageMessage(this.user, this.socket, text,groupUin);
	}

	@Override public void sendGroupXml(String text, long groupUin)
	{
		SendMessage.sendGroupXmlMessage(this.user, this.socket, text,groupUin);
	}

	@Override public void sendFriendMessage(String text, long friendUin)
	{
		SendMessage.sendFriendMessage(this.user, this.socket, text,friendUin);
	}
	
	private RingzuxHandler socket = null;
	
	private QQUser user = null;

	public RobotApi(RingzuxHandler _socket, QQUser _user)
	{
		this.user = _user;
		this.socket = _socket;
	}
	
	@Override public GroupList getGroupList()
	{
		return this.user.groupList;
	}

	@Override public FriendList getFriendList()
	{
		return this.user.friendList;
	}
}
