package com.lonecpp.client.netty;

import com.alibaba.fastjson.JSONObject;
import com.lonecpp.core.vo.DefaultFuture;
import com.lonecpp.core.vo.Result;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author seven sins
 * @date 2017年10月28日 下午2:18:38
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String ping = "ping";
		if (ping.equals(msg.toString())) {
			ctx.channel().writeAndFlush("ping\r\n");
			return;
		}
		
		// ctx.channel().attr(AttributeKey.valueOf("ChannelKey")).set(msg.toString());

		System.out.println("客户端返回数据===" + msg.toString());
		Result result = JSONObject.parseObject(msg.toString(), Result.class);
		DefaultFuture.recive(result);
		// ctx.channel().close();
	}

}
