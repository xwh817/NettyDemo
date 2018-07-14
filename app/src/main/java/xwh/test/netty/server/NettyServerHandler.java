package xwh.test.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import xwh.test.netty.utils.Logger;

/**
 * Created by xwh on 18-7-14.
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RequestInfoVO> {
    private static final String TAG= "NettyServerHandler";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestInfoVO msg) throws Exception {
        //
        Logger.d(TAG, msg.getBody());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }


    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {

    }

}
