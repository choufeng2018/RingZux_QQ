package com.Tick_Tock.PCTIM.Client;
import io.netty.channel.*;
import java.nio.charset.*;
import io.netty.channel.nio.*;
import io.netty.bootstrap.*;
import io.netty.channel.socket.nio.*;
import java.net.*;
import io.netty.channel.socket.*;
import io.netty.handler.codec.*;
import java.util.concurrent.*;
import com.Tick_Tock.PCTIM.*;

public class RingzuxClient implements Runnable
{
	/*
	 * 服务器端口号
	 */
	private int port;

	/*
	 * 服务器IP
	 */
	private String host;

	private RingzuxHandler handler;

	private Bootstrap bootstrap;

	private NioEventLoopGroup eventLoopGroup;

	public RingzuxClient(int port, String host)
	{
		System.out.println(host);
		this.port = port;
		this.host = host;
		this.eventLoopGroup = new NioEventLoopGroup();
		this.handler = new RingzuxHandler();
	}
	
	public RingzuxClient(int port, String host,QQUser user)
	{
		System.out.println(host);
		this.port = port;
		this.host = host;
		this.eventLoopGroup = new NioEventLoopGroup();
		this.handler = new RingzuxHandler(user);
	}

	public void run() 
	{
		try
		{
			this.bootstrap = new Bootstrap();
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.group(eventLoopGroup);
			bootstrap.remoteAddress(host,port);
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel)
					throws Exception
					{ 
						socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024*10,0,2,-2,0)).addLast(handler);
						/**/
					}
				});
			this.connect();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			
		}
	}

	
		
	

	private void connect() throws InterruptedException
	{

		ChannelFuture future = bootstrap.connect(host, port).sync();

		future.channel().closeFuture().sync();

	}
}
