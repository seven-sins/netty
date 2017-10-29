package com.lonecpp.server.netty;

import com.google.protobuf.ByteString;
import com.lonecpp.core.pb.RequestProbuf.PbRequest;
import com.lonecpp.core.pb.UserProbuf.PbUser;
import com.lonecpp.server.config.Media;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author seven sins
 * @date 2017年10月28日 下午2:15:49
 */
public class PbServerHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		PbRequest pbRequest = (PbRequest) msg;
		// 获取请求体
		ByteString byteString = pbRequest.getBody();
		PbUser pbUser = PbUser.parseFrom(byteString);
		
		System.out.println(pbUser.getName());
		
		// RequestParam
		
		//		if (msg instanceof ByteBuf) {
		//			ByteBuf req = (ByteBuf) msg;
		//			String content = req.toString(Charset.defaultCharset());
		//
		//			RequestParam request = JSONObject.parseObject(content, RequestParam.class);
					Object result = Media.execute(pbRequest);
		
					ctx.channel().write(result);
					ctx.channel().writeAndFlush("\r\n");
		//		}
		// ctx.writeAndFlush(pbUser);
		
		// ctx.channel().writeAndFlush("\r\n"); // .addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 客户端退出
		ctx.close();
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		super.channelWritabilityChanged(ctx);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event != null) {
				if (event.equals(IdleState.READER_IDLE)) {
					// 读空闲
					ctx.close();
				} else if (event.equals(IdleState.WRITER_IDLE)) {
					// 写空闲
					ctx.channel().writeAndFlush("ping\r\n");
				}
			}
		}

		super.userEventTriggered(ctx, evt);
	}

}
