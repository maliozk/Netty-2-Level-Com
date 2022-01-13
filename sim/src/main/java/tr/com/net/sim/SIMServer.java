package tr.com.net.sim;

import tr.com.net.common.connection.tcp.RDTCPServer;
import tr.com.net.message.IMessage;
import tr.com.net.message.Message1;
import tr.com.net.message.Message2;
import tr.com.net.message.MessageCodec;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SIMServer {
    public static void main(String[] args) {
        RDTCPServer<IMessage> rdtcpServer = new RDTCPServer<>(SIMConfig.PORT, new MessageCodec());
        rdtcpServer.start();
//
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Random random = new Random();
                int i = Math.abs(random.nextInt() % 2);
                if(i == 0)
                {
                    rdtcpServer.sendMessage(new Message1(1, 2));
                    System.out.println("Sending message 1");
                }
                else
                {
                    rdtcpServer.sendMessage(new Message2(4));
                    System.out.println("Sending message 2");
                }
            }
        }, 1000, 1000);

//        RDUDPServer<IMessage> udpServer = new RDUDPServer<>(SIMConfig.UDPPORT, new MessageDatagramCodec());
//        udpServer.start();

//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Random random = new Random();
//                int i = Math.abs(random.nextInt() % 2);
//                if(i == 0)
//                {
//                    udpServer.sendMessage(new Message1(1, 2));
//                    System.out.println("Sending message 1");
//                }
//                else
//                {
//                    udpServer.sendMessage(new Message2(4));
//                    System.out.println("Sending message 2");
//                }
//            }
//        }, 1000, 1000);
    }
}
