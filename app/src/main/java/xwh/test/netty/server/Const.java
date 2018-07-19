package xwh.test.netty.server;

/**
 * Created by xwh on 18-7-15.
 */

public class Const {
	public static final int SERVER_PORT = 7788;
    public static final int HEADER = 0x0CAFFEE0;    // 消息头部
    public static final byte VERSION = 1;        // 消息格式版本号
	public static final String DEFAULT_ENCODE = "UTF-8";
}
