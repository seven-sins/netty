package com.lonecpp.server.netty;

import java.nio.charset.Charset;

import com.alibaba.fastjson.JSONObject;
import com.lonecpp.core.vo.RequestParam;
import com.lonecpp.core.vo.Response;
import com.lonecpp.server.config.Media;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author seven sins
 * @date 2017年10月28日 下午2:15:49
 */
public class SimpleHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if(msg instanceof ByteBuf){
			ByteBuf req = (ByteBuf)msg;
			String content = req.toString(Charset.defaultCharset());
			System.out.println(content);
			RequestParam request = JSONObject.parseObject(content,RequestParam.class);
			Object result = Media.execute(request);
			
			Response res = new Response();
			res.setId(request.getId());
			res.setContent(result);
			ctx.channel().write(JSONObject.toJSONString(res));
			ctx.channel().writeAndFlush("\r\n");
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx)
			throws Exception {
		super.channelWritabilityChanged(ctx);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if(evt instanceof IdleStateEvent){
			IdleStateEvent event = (IdleStateEvent)evt;
			if(event != null) {
				if(event.equals(IdleState.READER_IDLE)){
					ctx.close();
				}else if(event.equals(IdleState.WRITER_IDLE)){
					
				}else if(event.equals(IdleState.WRITER_IDLE)){
					ctx.channel().writeAndFlush("ping\r\n");
				}
			}
		}
		
		super.userEventTriggered(ctx, evt);
	}

}
