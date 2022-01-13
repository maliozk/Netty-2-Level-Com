package tr.com.net.common.connection.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.log4j.Log4j2;
import tr.com.net.common.connection.ARDConnection;
import tr.com.net.common.connection.ConnectionState;

import java.util.concurrent.TimeUnit;

/**
 * UDP Client Connection implementation by Netty.
 *
 * @param <K>  message type
 * @author mozkeskin
 */
@Log4j2
public class RDUDPClient<K> extends ARDConnection<K> {

    /**
     * Server port
     */
    private int port;

    /**
     * Server IP
     */
    private String ip;

    /**
     * UDP Client constructor
     *
     * @param host server ip
     * @param port server port
     * @param handlers Netty handler array, added end of the pipeline in order
     */
    public RDUDPClient(String host, int port, ChannelHandler... handlers) {
        this.handlers = handlers;
        this.ip = host;
        this.port = port;
    }

    public void start() {
        try {
            eventLoopGroup = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(eventLoopGroup);
            b.channel(NioDatagramChannel.class);
            b.handler(new ChannelInitializer<DatagramChannel>() {
                @Override
                public void initChannel(DatagramChannel ch) throws Exception {
                    ch.pipeline().addLast(handlers);
                    ch.pipeline().addLast(new ConnectionHandler());
                }
            });
            b.remoteAddress("127.0.0.1", this.port);
            ChannelFuture channelFuture = b.bind(this.port).sync();
            this.channel = channelFuture.channel();
            channel.eventLoop().scheduleAtFixedRate(() -> {
                channel.flush();
            }, 100, 50, TimeUnit.MILLISECONDS);
            stateChanged(ConnectionState.CONNECTED);
        }
        catch (Exception e)
        {
            log.error(e);
        }
    }

    @Override
    public void sendMessage(K message) {
        if (channel != null && channel.isActive()) {
            channel.write(message);
        }
    }
}
