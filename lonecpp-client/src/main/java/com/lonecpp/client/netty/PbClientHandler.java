package com.lonecpp.client.netty;

import com.lonecpp.core.common.Constant;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

/**
 * @author seven sins
 * @date 2017年10月29日 下午7:54:32
 */
public class PbClientHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ctx.channel().attr(AttributeKey.valueOf(Constant.CHANNEL_KEY)).set(msg);
		ctx.channel().close();
	}
}
