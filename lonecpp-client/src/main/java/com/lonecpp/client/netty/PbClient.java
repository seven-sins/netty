package com.lonecpp.client.netty;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;

import com.lonecpp.core.common.Constant;
import com.lonecpp.core.pb.RequestProbuf.PbRequest;
import com.lonecpp.core.pb.ResponseProbuf.PbResponse;
import com.lonecpp.core.pb.UserProbuf.PbUser;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.util.AttributeKey;

/**
 * @author seven sins
 * @date 2017年10月28日 下午2:18:45
 */
public class PbClient {
	static final Logger LOG = Logger.getLogger(PbClient.class);
	public static EventLoopGroup group = null;
	public static Bootstrap boostrap = null;

	static {
		group = new NioEventLoopGroup();
		boostrap = new Bootstrap();
		boostrap.channel(NioSocketChannel.class);
		boostrap.group(group);
		boostrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		boostrap.handler(new ChannelInitializer<NioSocketChannel>() {
			@Override
			protected void initChannel(NioSocketChannel ch) throws Exception {
				ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
				ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
				ch.pipeline().addLast(new ProtobufEncoder());
				ch.pipeline().addLast(new ProtobufDecoder(PbResponse.getDefaultInstance()));
				//
				ch.pipeline().addLast(new PbClientHandler());
			}
		});
	}

	public static void main(String[] args) {
		try {
			ChannelFuture future = boostrap.connect(new InetSocketAddress("127.0.0.1", 7777)).sync();
			//
			PbUser.Builder pbUser = PbUser.newBuilder();
			pbUser.setId(1);
			pbUser.setPhone("111111111");
			pbUser.setName("maliodas");
			
			PbRequest.Builder pbRequest = PbRequest.newBuilder();
			pbRequest.setId(77777);
			pbRequest.setCommand("userAdd");
			pbRequest.setBody(pbUser.build().toByteString());
			
			future.channel().writeAndFlush(pbRequest);
			future.channel().closeFuture().sync();
			
			Object result = future.channel().attr(AttributeKey.valueOf(Constant.CHANNEL_KEY)).get();
			
			// PbResponse.parseFrom(result)
			
			System.out.println(result);
			
			
			
			
			
			
			
			
		} catch (Exception e) {
			LOG.error(e);
		} finally {
			group.shutdownGracefully();
		}
	}

}
