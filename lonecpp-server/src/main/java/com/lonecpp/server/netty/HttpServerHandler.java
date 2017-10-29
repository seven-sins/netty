package com.lonecpp.server.netty;

import java.nio.charset.Charset;

import com.alibaba.fastjson.JSONObject;
import com.lonecpp.core.common.Status;
import com.lonecpp.core.vo.RequestParam;
import com.lonecpp.core.vo.Result;
import com.lonecpp.server.config.Media;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * @author seven sins
 * @date 2017年10月29日 下午2:40:15
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof FullHttpRequest) {
			FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
			// 获取请求内容
			String content = fullHttpRequest.content().toString(Charset.defaultCharset());
			if(content == null || "".equals(content)) {
				write(ctx, new Result(Status.ERROR, "没有请求内容"));
				return;
			}
			RequestParam requestParam = JSONObject.parseObject(content, RequestParam.class);
			
			Result result = Media.execute(requestParam);
			
			write(ctx, result);
		}
	}

	private void write(ChannelHandlerContext ctx, Result result) {
		DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.wrappedBuffer(JSONObject.toJSONString(result).getBytes(Charset.defaultCharset())));
		// 设置headers
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
		response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		
		ctx.channel().writeAndFlush(response);
	}
	
}
