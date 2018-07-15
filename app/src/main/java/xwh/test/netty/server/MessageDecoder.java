package xwh.test.netty.server;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import xwh.test.netty.utils.Logger;

/**
 * Created by xwh on 18-7-14.
 */

public class MessageDecoder extends ByteToMessageDecoder {
    public MessageDecoder() {

    }
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 14) {
            Logger.e("MessageDecoder", "Error Length!");
            return;
        }
        // 标记开始读取位置
        in.markReaderIndex();

        int header = in.readInt();

        if (Const.HEADER != header) {
            Logger.e("MessageDecoder", "Error Header!");
            ctx.close();
            return;
        }

        byte version = in.readByte();
        int type = in.readInt();
        int squence = in.readInt();
        int length = in.readInt();

        if (length < 0) {
            ctx.close();
            return;
        }

        if (in.readableBytes() < length) {
            // 重置到开始读取位置
            in.resetReaderIndex();
            return;
        }

        byte[] body = new byte[length];
        in.readBytes(body);

        RequestInfoVO req = new RequestInfoVO();
        req.setBody(new String(body, "utf-8"));
        req.setType(type);
        req.setSequence(squence);

        out.add(req);


        Logger.e("MessageDecoder:", req.toJson().toString());

    }
}