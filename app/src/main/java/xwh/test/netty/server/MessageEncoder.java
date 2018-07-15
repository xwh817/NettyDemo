package xwh.test.netty.server;

import android.text.TextUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import xwh.test.netty.utils.Logger;

/**
 * Created by xwh on 18-7-14.
 */

public class MessageEncoder extends MessageToByteEncoder<RequestInfoVO> {

    private static final String DEFAULT_ENCODE = "utf-8";

    public MessageEncoder() {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, RequestInfoVO msg, ByteBuf out) throws Exception {

        if (msg == null) {
            return;
        }

        ByteBufOutputStream writer = new ByteBufOutputStream(out);

        writer.writeInt(Const.HEADER);

        writer.writeByte(Const.VERSION);

        writer.writeInt(msg.getType());

        writer.writeInt(msg.getSequence());

        byte[] body = null;

        if (!TextUtils.isEmpty(msg.getBody())) {
            body = msg.getBody().getBytes(DEFAULT_ENCODE);
        }

        if (null == body || 0 == body.length) {
            writer.writeInt(0);
        } else {
            writer.writeInt(body.length);
            writer.write(body);
        }

        Logger.e("MessageDecoder", msg.toJson().toString());
    }

}
