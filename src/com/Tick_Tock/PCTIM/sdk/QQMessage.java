package com.Tick_Tock.PCTIM.sdk;
import java.util.*;

public class QQMessage
{
	List<MessagePart> messageBody = new ArrayList<MessagePart>();

	public static final int type_text_message = 0;

	public static final int type_emoji_message = 1;

	public static final int type_at_message = 2;

	public static final int type_xml_message = 3;

	public static final int type_json_message = 4;

	public static final int type_image_message = 5;

	public static final int type_voice_message = 6;

	public static final int type_file_message = 7;

	public static final int type_red_packet_message = 8;

	public static final int type_noney_transfer_message = 9;

	public QQMessage addMoneyTransfer(String body)
	{
		this.messageBody.add(new MessagePart().setType(type_noney_transfer_message).setBody(body));
		return this;
	}

	public QQMessage addRedPacket(String body)
	{
		this.messageBody.add(new MessagePart().setType(type_red_packet_message).setBody(body));
		return this;
	}

	public QQMessage addText(String body)
	{
		this.messageBody.add(new MessagePart().setType(type_text_message).setBody(body));
		return this;
	}

	public QQMessage addImage(String body)
	{
		this.messageBody.add(new MessagePart().setType(type_image_message).setBody(body));
		return this;
	}

	public QQMessage addVoice(String body)
	{
		this.messageBody.add(new MessagePart().setType(type_voice_message).setBody(body));
		return this;
	}

	public QQMessage addEmoji(int body)
	{
		this.messageBody.add(new MessagePart().setType(type_emoji_message).setBody(body));
		return this;
	}

	public QQMessage addXml(String body)
	{
		this.messageBody.add(new MessagePart().setType(type_xml_message).setBody(body));
		return this;
	}

	public QQMessage addJson(String body)
	{
		this.messageBody.add(new MessagePart().setType(type_json_message).setBody(body));
		return this;
	}

	public QQMessage addFile(String body)
	{
		this.messageBody.add(new MessagePart().setType(type_file_message).setBody(body));
		return this;
	}
	
	public long senderUin = 0;
	
	public long messageId =0;

	public byte[] messageIndex;

	public long time;

	public String font;

	public String message = "";

	

	public long selfUin;
	
	
	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (MessagePart mp:this.messageBody)
		{
			switch (mp.type)
			{
				case type_text_message:{
						sb.append(mp.body.toString());
					}break;
				case type_voice_message:{
						sb.append("[voice:" + mp.body.toString() + "]");
					}break;
				case type_image_message:{
						sb.append("[image:" + mp.body.toString() + "]");
					}break;
				case type_xml_message:{
						sb.append("[xml:" + mp.body.toString() + "]");
					}break;
				case type_json_message:{
						sb.append("[json:" + mp.body.toString() + "]");
					}break;
				case type_emoji_message:{
						sb.append("[emoji:" + mp.body.toString() + "]");
					}break;
				case type_at_message:{
						sb.append("[at:" + ((AtStore)mp.body).atName + "(" + ((AtStore)mp.body).targetUin +")"+ "]");
					}break;
				case type_file_message:{
						sb.append("[file:" + mp.body.toString() + "]");
					}break;
				case type_red_packet_message:{
						sb.append("[redpacket:" + mp.body.toString() + "]");
					}break;
				case type_noney_transfer_message:{
						sb.append("[moneytransfer:" + mp.body.toString() + "]");
					}break;
				default:{
						System.out.println("未知消息片段类型");
					}
			}
		}
		return sb.toString();
	}
}
