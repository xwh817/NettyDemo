package xwh.netty.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import xwh.netty.server.Const;

/**
 * Created by xwh on 18-7-14.
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {


    public MessageEncoder() {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {

        if (msg == null) {
            return;
        }

        ByteBufOutputStream writer = new ByteBufOutputStream(out);

        writer.writeInt(Const.HEADER);

        writer.writeByte(Const.VERSION);

        writer.writeInt(msg.getType());

        writer.writeInt(msg.getSequence());

	int bodyLength = 0;	// 消息体长度
        byte[] body = null;
        if (msg.getBody() != null && msg.getBody().length() > 0) {
            body = msg.getBody().getBytes(Const.DEFAULT_ENCODE);
            bodyLength = body.length;
        }

        writer.writeInt(bodyLength);
        if (body != null) {
            writer.write(body);
        }
        
        //Logger.d("MessageEncoder:", msg.getBody() +" ( " + out.capacity() + "  <-- " + out.writerIndex());

    }

}
