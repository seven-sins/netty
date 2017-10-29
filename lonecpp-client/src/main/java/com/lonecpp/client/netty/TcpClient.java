package com.lonecpp.client.netty;

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

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.lonecpp.core.vo.DefaultFuture;
import com.lonecpp.core.vo.Request;
import com.lonecpp.core.vo.Result;

/**
 * @author seven sins
 * @date 2017年10月29日 下午2:14:26
 */
public class TcpClient {
	static final Logger LOG = Logger.getLogger(TcpClient.class);
	static EventLoopGroup GROUP = null;
	static Bootstrap BOOTSTRAP = null;
	static ChannelFuture FUTURE = null;
	
	static {
		GROUP = new NioEventLoopGroup();
		BOOTSTRAP = new Bootstrap();
		BOOTSTRAP.channel(NioSocketChannel.class);
		BOOTSTRAP.group(GROUP);
		BOOTSTRAP.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		BOOTSTRAP.option(ChannelOption.SO_KEEPALIVE, true).handler(new ChannelInitializer<NioSocketChannel>() {
			@Override
			protected void initChannel(NioSocketChannel ch) throws Exception {
				ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
				ch.pipeline().addLast(new StringDecoder());
				ch.pipeline().addLast(new ClientHandler());
				ch.pipeline().addLast(new StringEncoder());
			}
		});
		try {
			FUTURE = BOOTSTRAP.connect("localhost", 7777).sync();
		} catch (InterruptedException e) {
			LOG.error(e);
		}
	}

	/**
	 * 发送请求
	 * @param request
	 * @return
	 */
	public static Object send(Request request) {
		try {
			FUTURE.channel().writeAndFlush(JSONObject.toJSONString(request));
			FUTURE.channel().writeAndFlush("\r\n");
			DefaultFuture defaultFuture = new DefaultFuture(request);
			Result result = defaultFuture.get(10);
			return result;
		} catch (Exception e) {
			LOG.error(e);
		}
		return null;

	}

}
