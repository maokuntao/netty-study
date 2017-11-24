package com.taomaokun.netty.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Discards any incoming data.
 * 
 * @author taomaokun @since 2017年11月24日 下午10:47:41
 *
 */
public class DiscardService {

	private int port;

	public DiscardService(int port) {
		this.port = port;
	}

	public void run() throws InterruptedException {

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();

		try {

			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel sc) throws Exception {
							sc.pipeline().addLast(new DiscardServerHandler());
						}

					})
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			ChannelFuture future = b.bind(port).sync();
			future.channel().closeFuture().sync();

		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		int port = 8080;
		if(args.length>0) {
			port = Integer.parseInt(args[0]);
		}
		new DiscardService(port).run();
	}
}
