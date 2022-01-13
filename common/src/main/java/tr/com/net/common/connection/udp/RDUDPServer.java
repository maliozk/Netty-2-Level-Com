package tr.com.net.common.connection.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.log4j.Log4j2;
import tr.com.net.common.connection.ARDConnection;
import tr.com.net.common.connection.ConnectionState;

/**
 * UDP Server Connection implementation by Netty.
 *
 * @param <T>  message type
 * @author mozkeskin
 */
@Log4j2
public class RDUDPServer<T> extends ARDConnection<T> {
    /**
     * Server port
     */
    private int port;

    /**
     * UDP Server constructor
     *
     * @param port server port
     * @param handlers Netty channel handler array, added end of the pipeline in order
     */
    public RDUDPServer(int port, ChannelHandler... handlers) {
        this.port = port;
        this.handlers = handlers;
    }

    /**
     * Start the UDP Server connection
     */
    public void start() {

       eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup);
            bootstrap.channel(NioDatagramChannel.class);
            bootstrap.handler(new ChannelInitializer<DatagramChannel>() {
                @Override
                protected void initChannel(DatagramChannel channel) {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(handlers);
                    pipeline.addLast(new ConnectionHandler());
                }
            });
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            log.info("UDP Server is open at {}", port);

            channel = channelFuture.channel();
            stateChanged(ConnectionState.CONNECTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * No implementation, message sent by context accessed with message received
     *
     * @param message message
     */
    @Override
    public void sendMessage(T message) {
        // message sent by context accessed with message received
    }
}
