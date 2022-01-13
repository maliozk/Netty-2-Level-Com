package tr.com.net.center;

import tr.com.net.common.connection.tcp.RDTCPServer;
import tr.com.net.common.connection.tuple.RDConnectionTuple;
import tr.com.net.common.connection.udp.RDUDPClient;
import tr.com.net.common.connection.udp.RDUDPServer;
import tr.com.net.message.IMessage;
import tr.com.net.message.MessageCodec;
import tr.com.net.message.MessageDatagramDecoder;
import tr.com.net.message.MessageDatagramEncoder;
import tr.com.net.center.config.TupleConnectionConfigID;
import tr.com.net.center.connection.CenterConnectionHandler;

import java.net.InetSocketAddress;
import java.util.List;

public class Center {
    public static void main(String[] args) {

        CenterConnectionHandler handler = new CenterConnectionHandler();

        List<TupleConnectionConfigID> startConfig = CenterConfig.startConfig;
        startConfig.forEach(config -> addStartConnection(config, handler));

        List<TupleConnectionConfigID> endConfig = CenterConfig.endConfig;
        endConfig.forEach(config -> addEndConfig(config, handler));
    }

    private static void addEndConfig(TupleConnectionConfigID config, CenterConnectionHandler handler) {
        RDTCPServer<IMessage> tcp = new RDTCPServer<>(config.getPort(), new MessageCodec());
        InetSocketAddress socketAddressDyb = new InetSocketAddress(config.getIp(), config.getUdpPort());
        RDUDPClient<IMessage> udp = new RDUDPClient<>(config.getIp(), config.getUdpPort(), new MessageDatagramEncoder(socketAddressDyb));
        RDConnectionTuple<IMessage> connection = new RDConnectionTuple<>(config.getId(), tcp, udp);
        handler.addEndConnection(config.getId(), connection);
        connection.start();
    }

    private static void addStartConnection(TupleConnectionConfigID config, CenterConnectionHandler handler) {
        RDTCPServer<IMessage> tcp = new RDTCPServer<>(config.getPort(), new MessageCodec());
        RDUDPServer<IMessage> udp = new RDUDPServer<>(config.getUdpPort(), new MessageDatagramDecoder());
        RDConnectionTuple<IMessage> connection = new RDConnectionTuple<>(config.getId(), tcp, udp);
        handler.addStartConnection(config.getId(), connection);
        connection.start();

    }
}
