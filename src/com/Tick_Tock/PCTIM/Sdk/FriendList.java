package com.Tick_Tock.PCTIM.Sdk;
import java.util.*;

public class FriendList
{
	public List<Friend> members =new ArrayList<Friend>();

	public Friend getFriendObj(){
		return new Friend();
	}

	public class Friend{
		
		public String friendName;
		
		public long friendUin;

		public Friend setFriendName(String name){
			this.friendName=name;
			return this;
		}
		
		public Friend setFriendUin(long uin){
			this.friendUin=uin;
			return this;
		}
		
	}
}
