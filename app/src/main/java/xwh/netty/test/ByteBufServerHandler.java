package xwh.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 测试Netty的粘包问题，一次发送多条消息时，不是多次调用channelRead，而是多个消息都在同一个ByteBuf里面返回了。
 * Created by xwh on 2018/7/25.
 */
public class ByteBufServerHandler extends ChannelInboundHandlerAdapter {
	private int count;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, "UTF-8");
		count++;
		System.out.println("body: " + body + ";" + count);
		/*String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? System.currentTimeMillis() + "" : "BAD ORDER";
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.writeAndFlush(resp);*/
	}
}
