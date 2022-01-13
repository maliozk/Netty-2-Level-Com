package tr.com.net.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;
import java.util.List;

@ChannelHandler.Sharable
public class MessageDatagramCodec extends MessageToMessageCodec<DatagramPacket, IMessage> {

    @Getter
    @Setter
    private InetSocketAddress socketAddress;

    public MessageDatagramCodec(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, IMessage iMessage, List<Object> list) throws Exception {
        System.out.println("Sending " + iMessage.getType());
        list.add(new DatagramPacket(Unpooled.wrappedBuffer(iMessage.encode()), socketAddress));
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket packet, List<Object> list) throws Exception {
        ByteBuf byteBuf = packet.content();
        byte f1 = byteBuf.readByte();
        int i = byteBuf.readableBytes();
        System.out.println(i);
        if (f1 == 1) {
            int data1 = byteBuf.readInt();
            int data2 = byteBuf.readInt();
            list.add(new Message1(data1, data2));
            System.out.println("1");
        } else if (f1 == 2) {
            int data1 = byteBuf.readInt();
            list.add(new Message2(data1));
            System.out.println("2");
        }

    }
}
