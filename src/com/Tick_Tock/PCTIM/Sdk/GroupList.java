package com.Tick_Tock.PCTIM.Sdk;
import java.util.*;

public class GroupList
{
	public List<Group> joinedGroup =new ArrayList<Group>();
	
	public List<Group> managedGroup =new ArrayList<Group>();
	
	public List<Group> createdGroup =new ArrayList<Group>();
	
	public List<Group> getAllGroup(){
		List<Group> all = new ArrayList<Group>();
		all.addAll(joinedGroup);
		all.addAll(managedGroup);
		all.addAll(createdGroup);
		return all;
	}
	
	public Group getgroupobj(){
		return new Group();
	}
	
	public class Group{
		public String groupName;
		public long groupUin;
		public long ownerUin;

		public Group setGroupName(String name){
			this.groupName=name;
			return this;
		}
		public Group setGroupUin(long uin){
			this.groupUin=uin;
			return this;
		}
		public Group setOwnerUin(long uin){
			this.ownerUin=uin;
			return this;
		}
}




}
