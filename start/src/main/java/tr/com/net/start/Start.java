package tr.com.net.start;

import tr.com.net.common.connection.tcp.RDTCPClient;
import tr.com.net.common.connection.tuple.RDConnectionTuple;
import tr.com.net.common.connection.udp.RDUDPClient;
import tr.com.net.start.connection.StartConnectionHandler;
import tr.com.net.message.IMessage;
import tr.com.net.message.MessageCodec;
import tr.com.net.message.MessageDatagramEncoder;

import java.net.InetSocketAddress;

public class Start {
    public static void main(String[] args) {
        RDTCPClient<IMessage> radarConnection = new RDTCPClient<>(StartConfig.simConfig.getIp(), StartConfig.simConfig.getPort(), new MessageCodec());

        RDTCPClient<IMessage> centerTCP1 = new RDTCPClient<>(StartConfig.centerConfig1.getIp(), StartConfig.centerConfig1.getPort(), new MessageCodec());
        InetSocketAddress socketAddress1 = new InetSocketAddress(StartConfig.centerConfig1.getIp(), StartConfig.centerConfig1.getUdpPort());
        RDUDPClient<IMessage> centerUDP1 = new RDUDPClient<>(StartConfig.centerConfig1.getIp(), StartConfig.centerConfig1.getUdpPort(), new MessageDatagramEncoder(socketAddress1));
        RDConnectionTuple<IMessage> connectionConfig1 = new RDConnectionTuple<>(20, centerTCP1, centerUDP1);

        RDTCPClient<IMessage> centerTCP2 = new RDTCPClient<>(StartConfig.centerConfig2.getIp(), StartConfig.centerConfig2.getPort(), new MessageCodec());
        InetSocketAddress socketAddress2 = new InetSocketAddress(StartConfig.centerConfig2.getIp(), StartConfig.centerConfig2.getUdpPort());
        RDUDPClient<IMessage> centerUDP2 = new RDUDPClient<>(StartConfig.centerConfig2.getIp(), StartConfig.centerConfig2.getUdpPort(), new MessageDatagramEncoder(socketAddress2));
        RDConnectionTuple<IMessage> connectionConfig2 = new RDConnectionTuple<>(20, centerTCP2, centerUDP2);

        StartConnectionHandler connectionHandler = new StartConnectionHandler(radarConnection, connectionConfig1, connectionConfig2);
        radarConnection.start();
        connectionConfig1.start();
        connectionConfig2.start();

    }
}
