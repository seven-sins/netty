package com.lonecpp.client.netty;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.lonecpp.core.po.User;
import com.lonecpp.core.vo.Request;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;

/**
 * @author seven sins
 * @date 2017年10月28日 下午2:18:45
 */
public class NettyClient {
	static final Logger LOG = Logger.getLogger(NettyClient.class);
	public static EventLoopGroup group = null;
	public static Bootstrap boostrap = null;

	static {
		group = new NioEventLoopGroup();
		boostrap = new Bootstrap();
		boostrap.channel(NioSocketChannel.class);
		boostrap.group(group);
		boostrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		boostrap.option(ChannelOption.SO_KEEPALIVE, true).handler(new ChannelInitializer<NioSocketChannel>() {
			@Override
			protected void initChannel(NioSocketChannel ch) throws Exception {
				ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
				ch.pipeline().addLast(new StringDecoder());
				ch.pipeline().addLast(new ClientHandler());
				ch.pipeline().addLast(new StringEncoder());
			}
		});
	}

	public static void main(String[] args) {
		try {
			ChannelFuture future = boostrap.connect(new InetSocketAddress("127.0.0.1", 7777)).sync();
			//
			User user = new User();
			user.setAge(11);
			user.setId(1);
			user.setName("maliodas");

			Request request = new Request();
			request.setCommand("userSave");
			request.setContent(user);
			future.channel().writeAndFlush(JSONObject.toJSONString(request));
			future.channel().writeAndFlush("\r\n");
			future.channel().closeFuture().sync();
			Object result = future.channel().attr(AttributeKey.valueOf("ChannelKey")).get();
			System.out.println(result);
		} catch (Exception e) {
			LOG.error(e);
		} finally {
			group.shutdownGracefully();
		}
	}

}
