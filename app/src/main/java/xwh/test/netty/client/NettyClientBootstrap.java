package xwh.test.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import xwh.test.netty.server.Const;
import xwh.test.netty.server.Message;
import xwh.test.netty.server.MessageDecoder;
import xwh.test.netty.server.MessageEncoder;
import xwh.test.netty.utils.ByteUtil;
import xwh.test.netty.utils.Logger;

/**
 * Created by xwh on 18-7-14.
 *
 * https://blog.csdn.net/weihao_/article/details/72780444
 */

public class NettyClientBootstrap {
	private static final String TAG = "NettyServerBootstrap";

	private int port;
    private String host;
    private SocketChannel mSocketChannel;
    private Bootstrap mBootstrap;
    public NettyClientBootstrap(int port, String host) {
        this.host = host;
        this.port = port;
	    init();
    }
    private void init() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
	    mBootstrap = new Bootstrap();
        mBootstrap.channel(NioSocketChannel.class);
        mBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        mBootstrap.option(ChannelOption.TCP_NODELAY, true);
        mBootstrap.group(eventLoopGroup);
        mBootstrap.remoteAddress(this.host, this.port);
        mBootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
	            mSocketChannel = socketChannel;

	            mSocketChannel.pipeline().addLast(new MessageDecoder(),
			            new MessageEncoder(),
			            new NettyClientHandler(),
			            new DelimiterBasedFrameDecoder(1024, false, Unpooled.copiedBuffer(ByteUtil.toBytes(Const.HEADER)))
	            );
	            //Logger.d(TAG, "initChannel:" + socketChannel.localAddress().getHostString() + ", " + socketChannel.remoteAddress().getHostString());
            }
        });

    }

    public void connect() {
	    ChannelFuture future = null;
	    try {
		    future = mBootstrap.connect(this.host, this.port).sync();
	    } catch (InterruptedException e) {
		    e.printStackTrace();
	    }
	    if (future.isSuccess()) {
		    Logger.d(TAG,"connect server success");
	    }
    }

    public void disconnect() {
    	mSocketChannel.close();
    }

    public void sendMessage(Message message) {
    	mSocketChannel.writeAndFlush(message);
    }

    public static void main(String[] args) throws Exception {
        NettyClientBootstrap bootstrap = new NettyClientBootstrap(Const.SERVER_PORT, "127.0.0.1");
        bootstrap.connect();


        for (int i = 1; i<= 100; i++) {
            //TimeUnit.SECONDS.sleep(2);
            Message req = new Message();
            req.setType(1);
            req.setSequence(i);
            req.setBody("message from client " + i);
            bootstrap.sendMessage(req);
            i++;
        }
    }
}
