package xwh.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import xwh.netty.message.Message;
import xwh.netty.message.MessageDecoder;
import xwh.netty.message.MessageEncoder;
import xwh.netty.server.Const;
import xwh.netty.utils.Logger;

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

	            mSocketChannel.pipeline().addLast(
	            		new MessageDecoder(),
			            new MessageEncoder(),
			            new NettyClientHandler()
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

    public static void main(String[] args) {
        NettyClientBootstrap bootstrap = new NettyClientBootstrap(Const.SERVER_PORT, "127.0.0.1");
        bootstrap.connect();

	    StringBuilder stringBuilder = new StringBuilder();
	    for (int i = 1; i<= 1000; i++) {
	    	stringBuilder.append("message from client ");
	    }

        for (int i = 1; i<= 10; i++) {
            //TimeUnit.SECONDS.sleep(2);
            Message req = new Message();
            req.setType(1);
            req.setSequence(i);

            req.setBody(stringBuilder.toString());
            bootstrap.sendMessage(req);
            //bootstrap.sendString("message from client " + i);
        }
    }
}
