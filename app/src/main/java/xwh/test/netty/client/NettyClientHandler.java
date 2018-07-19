package xwh.test.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import xwh.test.netty.server.Message;

/**
 * Created by xwh on 18-7-14.
 */

public class NettyClientHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        System.out.println(msg.getBody());
        /*RequestInfoVO req = new RequestInfoVO();
        req.setSequence(msg.getSequence());
        req.setType(msg.getType());
        if (2 == msg.getType()) {
            req.setBody("client");
            ctx.channel().writeAndFlush(req);
        } else if (3 == msg.getType()) {
            req.setBody("zpksb");
            ctx.channel().writeAndFlush(req);
        }*/

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }
}
