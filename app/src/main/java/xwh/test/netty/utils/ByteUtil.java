package xwh.test.netty.utils;

/**
 * Created by xwh on 2018/7/19.
 */
public class ByteUtil {
	public static byte[] toBytes(int data)
	{
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (data & 0xff);
		bytes[1] = (byte) ((data & 0xff00) >> 8);
		bytes[2] = (byte) ((data & 0xff0000) >> 16);
		bytes[3] = (byte) ((data & 0xff000000) >> 24);
		return bytes;
	}

}
