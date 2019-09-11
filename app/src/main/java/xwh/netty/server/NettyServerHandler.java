package xwh.netty.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
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


    /**
     * 管道中事件触发
     */
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){  // 空闲状态事件
            IdleStateEvent event = (IdleStateEvent)evt;

            if(event.state() == IdleState.READER_IDLE) {
                Logger.d(TAG, "READER_IDLE");
            } else if(event.state() == IdleState.WRITER_IDLE) {
                Logger.d(TAG, "WRITER_IDLE");
            } else if(event.state() == IdleState.ALL_IDLE){
                Logger.d(TAG, "ALL_IDLE");
                //清除超时会话
                ChannelFuture writeAndFlush = ctx.writeAndFlush(new Message(2, "会话超时，断开连接！"));
                writeAndFlush.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) {
                        ctx.channel().close();
                    }
                });
            }
        }else{
            super.userEventTriggered(ctx, evt); // 其他事件使用默认
        }
    }
}
