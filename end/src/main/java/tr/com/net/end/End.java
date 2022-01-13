package tr.com.net.end;

import io.netty.channel.ChannelHandlerContext;
import tr.com.net.common.connection.ConnectionState;
import tr.com.net.common.connection.IRDConnectionListener;
import tr.com.net.common.connection.udp.RDUDPClient;
import tr.com.net.message.IMessage;
import tr.com.net.message.Message1;
import tr.com.net.message.Message2;
import tr.com.net.message.MessageDatagramCodec;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

public class End {
    public static void main(String[] args) {
        RDUDPClient<IMessage> udp = new RDUDPClient<>("127.0.0.1",5112, new MessageDatagramCodec(new InetSocketAddress(5113)));
        RDUDPClient<IMessage> udp2 = new RDUDPClient<>("127.0.0.1", 5113, new MessageDatagramCodec(new InetSocketAddress(5112)));

        udp2.start();
        udp.start();

        udp.addListener(new IRDConnectionListener<IMessage>() {
            @Override
            public void messageReceived(ChannelHandlerContext ctx, IMessage message) {
                System.out.println("Received message 1 " + message.getType());
            }

            @Override
            public void stateChanged(ConnectionState oldState, ConnectionState newState) {

            }
        });

        udp2.addListener(new IRDConnectionListener<IMessage>() {
            @Override
            public void messageReceived(ChannelHandlerContext ctx, IMessage message) {
                System.out.println("Received message 2 " + message.getType());
            }

            @Override
            public void stateChanged(ConnectionState oldState, ConnectionState newState) {

            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                udp.sendMessage(new Message2(12));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                udp2.sendMessage(new Message1(12, 24));
            }
        }, 1000, 1000);
    }

}
