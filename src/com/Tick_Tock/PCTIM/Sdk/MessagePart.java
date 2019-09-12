package com.Tick_Tock.PCTIM.Sdk;

public class MessagePart
{
	public int type;

	public Object body;

	public MessagePart setType(int type)
	{
		this.type = type;
		return this;
	}

	public MessagePart setBody(Object body)
	{
		this.body = body;
		return this;
	}
}

