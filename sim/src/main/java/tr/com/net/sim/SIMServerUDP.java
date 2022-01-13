package tr.com.net.sim;

import tr.com.net.common.connection.udp.RDUDPClient;
import tr.com.net.message.IMessage;
import tr.com.net.message.Message1;
import tr.com.net.message.Message2;
import tr.com.net.message.MessageDatagramEncoder;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SIMServerUDP {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        InetSocketAddress socketAddress = new InetSocketAddress(host, SIMConfig.UDPPORT);
        RDUDPClient<IMessage> udpServer = new RDUDPClient<>(host, SIMConfig.UDPPORT, new MessageDatagramEncoder(socketAddress));
        udpServer.start();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Random random = new Random();
                int i = Math.abs(random.nextInt() % 2);
                if(i == 0)
                {
                    udpServer.sendMessage(new Message1(1, 2));
                    System.out.println("Sending message 1");
                }
                else
                {
                    udpServer.sendMessage(new Message2(4));
                    System.out.println("Sending message 2");
                }
            }
        }, 1000, 1000);
    }
}
