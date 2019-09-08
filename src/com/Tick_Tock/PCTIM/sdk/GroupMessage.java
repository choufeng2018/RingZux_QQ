package com.Tick_Tock.PCTIM.sdk;

public class GroupMessage extends QQMessage
{
	public long groupUin;
	
	public String senderName;
	
	
	public QQMessage addAt(AtStore body)
	{
		this.messageBody.add(new MessagePart().setType(type_at_message).setBody(body));
		return this;
	}
}
