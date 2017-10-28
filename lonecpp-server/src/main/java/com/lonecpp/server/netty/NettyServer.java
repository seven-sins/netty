package com.lonecpp.server.netty;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author seven sins
 * @date 2017年10月28日 上午12:49:25
 */
@Component
public class NettyServer implements ApplicationListener<ContextRefreshedEvent>, Ordered {
	static final Logger LOGGER = Logger.getLogger(NettyServer.class);
	
	public void start(){
		EventLoopGroup parentGroup = new NioEventLoopGroup(3);
		EventLoopGroup childGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(parentGroup, childGroup);
			serverBootstrap.channel(NioServerSocketChannel.class);
			serverBootstrap.option(ChannelOption.SO_BACKLOG, 128)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch)
						throws Exception {
					ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE,Delimiters.lineDelimiter()[0]));
					/**
					 * 60 读超时
					 * 20 写超时
					 */
					ch.pipeline().addLast(new IdleStateHandler(60, 20, 15, TimeUnit.SECONDS));
					ch.pipeline().addLast(new SimpleHandler());
					ch.pipeline().addLast(new StringEncoder());
				}
			});
			
			ChannelFuture future = serverBootstrap.bind(7777).sync();
			future.channel().closeFuture().sync();
		}catch(Exception e){
			LOGGER.error("=============netty服务启动出错=============", e);
		}
	}

	@Override
	public int getOrder() {
		return -1;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		start();
	}

}
