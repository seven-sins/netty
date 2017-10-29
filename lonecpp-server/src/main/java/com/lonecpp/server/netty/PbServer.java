package com.lonecpp.server.netty;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author seven sins
 * @date 2017年10月28日 上午12:49:25
 */
@Component
public class PbServer implements ApplicationListener<ContextRefreshedEvent>, Ordered {
	static final Logger LOGGER = Logger.getLogger(PbServer.class);
	
	public void start(){
		EventLoopGroup parentGroup = new NioEventLoopGroup(3);
		EventLoopGroup childGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(parentGroup, childGroup);
			serverBootstrap.channel(NioServerSocketChannel.class);
			serverBootstrap.option(ChannelOption.SO_BACKLOG, 128)
            // .option(ChannelOption.SO_KEEPALIVE, true)
            .childHandler(new PbChannelInitializer());
			
			ChannelFuture future = serverBootstrap.bind(7777).sync();
			
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			LOGGER.error("=============netty服务启动出错=============", e);
		} finally {
			childGroup.shutdownGracefully();
			parentGroup.shutdownGracefully();
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
