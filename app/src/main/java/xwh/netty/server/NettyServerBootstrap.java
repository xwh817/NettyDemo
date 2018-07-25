package xwh.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import xwh.netty.message.MessageDecoder;
import xwh.netty.message.MessageEncoder;
import xwh.netty.utils.Logger;

/**
 * Created by xwh on 18-7-14.
 */

public class NettyServerBootstrap {
    private static final String TAG = "NettyServerBootstrap";

    private ServerBootstrap mServerBootstrap;

    public NettyServerBootstrap() {
        init();
    }

    private void init() {
        // 连接处理group
        EventLoopGroup boss = new NioEventLoopGroup();
        // 事件处理group
        EventLoopGroup worker = new NioEventLoopGroup();
        mServerBootstrap = new ServerBootstrap();
        // 绑定处理group
        mServerBootstrap.group(boss, worker);
        mServerBootstrap.channel(NioServerSocketChannel.class);
        // 保持连接数
        mServerBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        // 有数据立即发送
        mServerBootstrap.option(ChannelOption.TCP_NODELAY, true);
        // 保持连接
        mServerBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        // 处理新连接
        mServerBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel sc) {

               // Logger.d(TAG, "initChannel:" + sc.localAddress().getHostString() + ", " + sc.remoteAddress().getHostString());

                /**
                 * 粘包问题 https://blog.csdn.net/a123638/article/details/54377934
                 */
                // 增加任务处理
                ChannelPipeline p = sc.pipeline();
                p.addLast(
                        new MessageDecoder(),
                        new MessageEncoder(),
                        new NettyServerHandler()
                );
            }
        });

    }

    public void bind() {
        ChannelFuture f = null;
        try {
            f = mServerBootstrap.bind(Const.SERVER_PORT).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (f.isSuccess()) {
            Logger.d(TAG, "Server started success");
        } else {
            Logger.e(TAG, "long connection started fail");
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
