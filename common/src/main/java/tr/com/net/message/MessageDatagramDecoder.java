package tr.com.net.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

@ChannelHandler.Sharable
public class MessageDatagramDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket packet, List<Object> list) throws Exception {
        ByteBuf byteBuf = packet.content();
        byte f1 = byteBuf.readByte();
        int i = byteBuf.readableBytes();
        if (f1 == 1) {
            int data1 = byteBuf.readInt();
            int data2 = byteBuf.readInt();
            list.add(new Message1(data1, data2));
        } else if (f1 == 2) {
            int data1 = byteBuf.readInt();
            list.add(new Message2(data1));
        }

    }
}
