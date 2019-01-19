package xwh.netty.server;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import xwh.netty.message.MessageDecoder;
import xwh.netty.message.MessageEncoder;
import xwh.netty.utils.Logger;

/**
 * Created by xwh on 18-7-14.
 */

public class NettyServerBootstrap {
    private static final String TAG = "NettyServerBootstrap";

    private ServerBootstrap mServerBootstrap;
    private EventLoopGroup mBossGroup;  // 连接处理group
    private EventLoopGroup mWorkerGroup;    // 事件处理group

    public NettyServerBootstrap() {
        init();
    }

    private void init() {

        mBossGroup = new NioEventLoopGroup();
        mWorkerGroup = new NioEventLoopGroup();

        mServerBootstrap = new ServerBootstrap();
        // 绑定处理group
        mServerBootstrap.group(mBossGroup, mWorkerGroup);

	    // 设置Socket工厂
        mServerBootstrap.channel(NioServerSocketChannel.class);
        
        // 设置管道工厂
        mServerBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {

               // Logger.d(TAG, "initChannel:" + sc.localAddress().getHostString() + ", " + sc.remoteAddress().getHostString());
                // 增加任务处理
                socketChannel.pipeline().addLast(
                        new IdleStateHandler(5,7,10, TimeUnit.SECONDS),
                        new MessageDecoder(),
                        new MessageEncoder(),
                        new NettyServerHandler()
                );
            }
        });

        /**
        * TCP参数设置
        */
        /*mServerBootstrap.option(ChannelOption.SO_BACKLOG, 1024);// 保持连接数
        mServerBootstrap.option(ChannelOption.TCP_NODELAY, true);// 有数据立即发送
        mServerBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);// 保持连接
*/
    }

    public void bind() {
        try {
            ChannelFuture channelFuture = mServerBootstrap.bind(Const.SERVER_PORT).sync();
            Logger.d(TAG, "Start Server at " + Const.SERVER_PORT + ", success: " + channelFuture.isSuccess());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //mBossGroup.shutdownGracefully();
            //mWorkerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        try {
            new NettyServerBootstrap().bind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
