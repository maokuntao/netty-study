package com.taomaokun.netty.example;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <pre>
 * Handles a server-side channel.
 * {@link http://netty.io/wiki/user-guide-for-4.x.html}
 * 
 * The DISCARD Protocol.
 * {@link https://tools.ietf.org/html/rfc863}
 * </pre>
 * 
 * @author taomaokun
 *
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
		System.out.println("Received data from client.");
		System.out.println("The type of message is : " + msg.getClass().getName());
		// Discard the received data silently.
		((ByteBuf) msg).release(); // (3)
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
}

// （1）：http://netty.io/4.0/api/io/netty/channel/ChannelInboundHandler.html
// （2）：每当接收到来自client的消息，此方法就会被调用。在此示例中，接收的消息类型是ByteBuf（http://netty.io/4.0/api/io/netty/buffer/ByteBuf.html）
// (3): To implement the DISCARD protocol, the handler has to ignore the received message.
// 		ByteBuf is a reference-counted object which has to be released explicitly via the release() method.
// 		Please keep in mind that it is the handler's responsibility to release any reference-counted object passed to the handler.
// 		通常，会在channelRead方法体内的finally域内调用ReferenceCountUtil.release(msg);来释放连接。
// （4）：The exceptionCaught() event handler method is called with a Throwable
//		when an exception was raised by Netty due to an I/O error
// 		or by a handler implementation due to the exception thrown while processing events.
//		In most cases, the caught exception should be logged and its associated channel should be closed here,
// 		although the implementation of this method can be different depending on what you want to do to deal with an exceptional situation.
// 		For example, you might want to send a response message with an error code before closing the connection.
