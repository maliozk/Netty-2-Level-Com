package tr.com.net.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

@ChannelHandler.Sharable
public class MessageCodec extends MessageToMessageCodec<ByteBuf, IMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, IMessage iMessage, List<Object> list) throws Exception {
        list.add(Unpooled.wrappedBuffer(iMessage.encode()));
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int checkPoint = byteBuf.readerIndex();
        try {
            byte f1 = byteBuf.readByte();
            if (f1 == 1) {
                int data1 = byteBuf.readInt();
                int data2 = byteBuf.readInt();
                list.add(new Message1(data1, data2));
            } else if (f1 == 2) {
                int data1 = byteBuf.readInt();
                list.add(new Message2(data1));
            }
        }
        catch (DecoderException ex)
        {
            byteBuf.readerIndex(checkPoint);
        }
    }
}
