package com.Tick_Tock.PCTIM.Robot;
import com.Tick_Tock.PCTIM.sdk.*;

import com.Tick_Tock.PCTIM.*;
import com.Tick_Tock.PCTIM.Message.*;
import com.Tick_Tock.PCTIM.Utils.*;
import com.Tick_Tock.PCTIM.Client.*;

public class RobotApi implements API
{

	@Override
	public void SendFriendXml(String text, long friendUin)
	{
		SendMessage.SendFriendXmlMessage(this.user, this.socket, text,friendUin);
		
	}


	@Override
	public void SendGroupMessage(String text, long groupUin)
	{
		SendMessage.SendGroupMessage(this.user, this.socket, text,groupUin);
	}

	@Override
	public void SendGroupImage(String text, long groupUin)
	{
		SendMessage.SendGroupImageMessage(this.user, this.socket, text,groupUin);
	}

	@Override
	public void SendGroupXml(String text, long groupUin)
	{
		SendMessage.SendGroupXmlMessage(this.user, this.socket, text,groupUin);
	}

	@Override
	public void SendFriendMessage(String text, long friendUin)
	{
		SendMessage.SendFriendMessage(this.user, this.socket, text,friendUin);
	}
	
	private RingzuxHandler socket = null;
	private QQUser user = null;

	public RobotApi(RingzuxHandler _socket, QQUser _user)
	{
		this.user = _user;
		this.socket = _socket;
	}
	
	@Override
	public Group_List getgrouplist()
	{

		return this.user.group_list;
	}

	@Override
	public Friend_List getfriendlist()
	{
		return this.user.friend_list;
	}
}
