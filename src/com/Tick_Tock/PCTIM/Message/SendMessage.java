package com.Tick_Tock.PCTIM.Message;
import com.Tick_Tock.PCTIM.Package.*;
import com.Tick_Tock.PCTIM.*;
import com.Tick_Tock.PCTIM.Sdk.*;
import com.Tick_Tock.PCTIM.Utils.*;
import com.Tick_Tock.PCTIM.Client.*;

public class SendMessage
{

	public static void sendFriendXmlMessage(QQUser user, RingzuxHandler socket, String message, long friendUin)
	{
		System.out.println("[机器人] [好友消息 发送] 到 " + friendUin + " [消息] " + message);
		byte[] data = SendPackage.get00cd(user, message, friendUin,1);
		socket.send(data);
	}

	public static void sendGroupXmlMessage(QQUser user, RingzuxHandler socket, String message, long groupUin)
	{
		System.out.println("[机器人] [群消息 发送] 到群 " + groupUin + " [消息] " + message);
		byte[] data = SendPackage.get0002(user, message, groupUin, 1);
		socket.send(data);
	}

	public static void sendGroupImageMessage(QQUser user, RingzuxHandler socket, String message, long groupUin)
	{
		System.out.println("[机器人] [群消息 发送] 到群 " + groupUin + " [消息] " + message);
		byte[] data = SendPackage.get0388(user, message, groupUin);
		socket.send(data);
	}
	public static void sendGroupMessage(final QQUser user, final RingzuxHandler socket, final String message, final long groupUin)
	{
		System.out.println("[机器人] [群消息 发送] 到群 " + groupUin + " [消息] " + message);
		byte[] data = SendPackage.get0002(user, message, groupUin, 0);
		socket.send(data);
	}


	public static void sendFriendMessage(final QQUser user, final RingzuxHandler socket, final String message, final long friendUin)
	{
		System.out.println("[机器人] [好友消息 发送] 到 " + friendUin + " [消息] " + message);
		byte[] data = SendPackage.get00cd(user, message, friendUin,0);
		socket.send(data);
	}

}
