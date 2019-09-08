package com.Tick_Tock.PCTIM.sdk;

public class AtStore
{
	/**
	 * 艾特的目标qq
	 */
	public long targetUin;

	/**
	 * 艾特的目标名字，可以随意设置
	 */
	public String atName;

	/**
	 * 设置艾特目标qq
	 * @param uin qq号
	 * @return 返回自身
	 */
	public AtStore setTargetUin(long uin)
	{
		this.targetUin = uin;
		return this;
	}

	/**
	 * 设置艾特目标名字
	 * @param name 名字
	 * @return 返回自身
	 */
	public AtStore setAtName(String name)
	{
		this.atName = name;
		return this;
	}
}

