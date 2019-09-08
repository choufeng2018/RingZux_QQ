package com.Tick_Tock.PCTIM.Robot;
import java.net.*;
import com.Tick_Tock.PCTIM.sdk.*;
import com.Tick_Tock.PCTIM.*;
import com.Tick_Tock.PCTIM.Package.*;
import com.Tick_Tock.PCTIM.Message.*;
import com.Tick_Tock.PCTIM.Utils.*;
import java.util.*;
import java.io.*;
import com.Tick_Tock.PCTIM.Client.*;
import java.util.concurrent.*;
public class QQRobot
{
	private RingzuxHandler socket = null;
	private QQUser user = null;
	private RobotApi api;
	private String exact_directory;
	List<Plugin> plugins =new ArrayList<Plugin>();
	private ExecutorService threadpool = new ThreadPoolExecutor(50, 50, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

	public QQRobot(RingzuxHandler _socket, QQUser _user)
	{

		this.socket = _socket;
		this.user = _user;
		this.api = new RobotApi(this.socket, this.user);
		File directory = new File("");

		try
		{
			this.exact_directory  = directory.getCanonicalPath();
			File plugin_path = new File(exact_directory + "/plugin");
			String[] plugin_list = plugin_path.list();
			if (plugin_list != null)
			{
				List<String> list = Arrays.asList(plugin_list);
				for (String file: list)
				{
					if (file.endsWith(".jar"))
					{

						ClassLoader loader = new URLClassLoader(new URL[]{new URL("file://" + this.exact_directory + "/plugin/" + file)});
						Class<?> pluginCls = loader.loadClass("com.robot.Main");
						final Plugin plugin = (Plugin)pluginCls.getDeclaredConstructor().newInstance();
						if (plugin.name() != null)
						{
							this.plug(plugin);
						}
						else
						{
							System.out.println("[插件] 加载失败 : 插件名为null,拒绝加载. 文件名: " + file);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void plug(final Plugin plugin)
	{
		plugins.add(plugin);
		this.threadpool.submit(
			new Runnable(){
				@Override public void run()
				{
					try
					{
						plugin.onLoad(api);
					}
					catch (Exception e)
					{
						e.printStackTrace();

					}
				}
			});
		System.out.println("[插件] 加载成功 [插件名]: " + plugin.name());

	}


	public void unplug(String pluginname)
	{
		Plugin targetplugin =null;
		for (Plugin plugin:this.plugins)
		{
			if (plugin.name().equals(pluginname))
			{
				targetplugin = plugin;
				break;
			}
		}
		this.plugins.remove(targetplugin);
		System.out.println("[插件] 卸载成功 [插件名]: " + targetplugin.name());
	}

	public void plug(String filename)
	{

		try
		{
			ClassLoader loader = new URLClassLoader(new URL[]{new URL("file://" + this.exact_directory + "/plugin/" + filename)});
			Class<?> pluginCls = loader.loadClass("com.robot.Main");
			final Plugin waitedplugin = (Plugin)pluginCls.getDeclaredConstructor().newInstance();
			for (Plugin plugin:this.plugins)
			{
				if (plugin.name().equals(waitedplugin.name()))
				{
					return;
				}
			}
			this.plug(waitedplugin);
		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
	}



	public void call(final QQMessage qqmessage)
	{
		for (final Plugin plugin : this.plugins)
		{
			this.threadpool.submit(
				new Runnable(){
					public void run()
					{
						try
						{
							plugin.onMessageHandler(qqmessage);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				});
		}
	}

}




