package xwh.netty.message;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import xwh.netty.server.Const;
import xwh.netty.utils.Logger;

/**
 * Created by xwh on 18-7-14.
 */

public class MessageDecoder extends ByteToMessageDecoder {
    public MessageDecoder() {

    }
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //Logger.d("MessageDecoder", "readableBytes:" + in.readableBytes() + " / capacity:" + in.capacity());

        int readableBytes = in.readableBytes();
        // 这儿不知道每次发送过来的数据有多大，所以要判断大小。 最小长度：4 + 1 + 4 + 4 + 4
        if (readableBytes < 17) {
            return;
        }
        // 标记开始读取位置，有可能缓存消息不全，要等下次继续读
        in.markReaderIndex();

        int header = in.readInt();

        if (Const.HEADER != header) {   // 消息头错误，可能读取的下标错乱了
            Logger.e("MessageDecoder", "Error Header!");
            ctx.close();
            return;
        }

        byte version = in.readByte();
        int type = in.readInt();
        int squence = in.readInt();
        int length = in.readInt();

        if (in.readableBytes() < length) {  // 消息体可读取长度不够了，等下次
            // 这个配合markReaderIndex使用的。把readIndex重置到mark的地方，下次有数据来的时候继续从上次开始位置读
            in.resetReaderIndex();
            Logger.e("MessageDecoder", "goto next decode");
            return;
        }

        byte[] body = new byte[length];
        in.readBytes(body);

        Message msg = new Message();
        msg.setBody(new String(body, "utf-8"));
        msg.setType(type);
        msg.setSequence(squence);

        out.add(msg);

        //Logger.d("MessageDecoder", msg.getSequence() +","+ msg.getBody().length() +" ( "+ readableBytes + " / " + in.capacity() + "  <-- " + in.readerIndex());
        Logger.d("MessageDecoder", msg.toString());
    }
}