package xwh.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import xwh.netty.message.Message;
import xwh.netty.utils.Logger;

/**
 * Created by xwh on 18-7-14.
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<Message> {
    private static final String TAG= "NettyServerHandler";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) {

        //Logger.d(TAG, msg.getBody());

        // 收到消息后回复
        Message req = new Message();
        req.setType(2);
        req.setSequence(msg.getSequence());
        req.setBody("reply from server: " + msg.getSequence());
        ctx.writeAndFlush(req);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Logger.e(TAG, "exceptionCaught: " + cause.getMessage());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Logger.d(TAG, "channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Logger.d(TAG, "channelInactive");
    }


}
