package com.Tick_Tock.PCTIM.Message;
import com.Tick_Tock.PCTIM.Package.*;
import com.Tick_Tock.PCTIM.*;
import com.Tick_Tock.PCTIM.sdk.*;
import com.Tick_Tock.PCTIM.Utils.*;
import com.Tick_Tock.PCTIM.Client.*;

public class SendMessage
{

	public static void SendFriendXmlMessage(QQUser user, RingzuxHandler socket, String message, long friendUin)
	{
		Util.log("[机器人] [好友消息 发送] 到 " + friendUin + " [消息] " + message);
		byte[] data = SendPackage.get00cd(user, message, friendUin,1);
		socket.send(data);
	}

	public static void SendGroupXmlMessage(QQUser user, RingzuxHandler socket, String message, long groupUin)
	{
		Util.log("[机器人] [群消息 发送] 到群 " + groupUin + " [消息] " + message);
		byte[] data = SendPackage.get0002(user, message, groupUin, 1);
		socket.send(data);
	}

	public static void SendGroupImageMessage(QQUser user, RingzuxHandler socket, String message, long groupUin)
	{
		Util.log("[机器人] [群消息 发送] 到群 " + groupUin + " [消息] " + message);
		byte[] data = SendPackage.get0388(user, message, groupUin);
		socket.send(data);
	}
	public static void SendGroupMessage(final QQUser user, final RingzuxHandler socket, final String message, final long groupUin)
	{
		Util.log("[机器人] [群消息 发送] 到群 " + groupUin + " [消息] " + message);
		byte[] data = SendPackage.get0002(user, message, groupUin, 0);
		socket.send(data);
	}


	public static void SendFriendMessage(final QQUser user, final RingzuxHandler socket, final String message, final long friendUin)
	{
		Util.log("[机器人] [好友消息 发送] 到 " + friendUin + " [消息] " + message);
		byte[] data = SendPackage.get00cd(user, message, friendUin,0);
		socket.send(data);
	}

}
