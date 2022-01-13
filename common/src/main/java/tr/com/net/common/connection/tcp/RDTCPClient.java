package tr.com.net.common.connection.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.log4j.Log4j2;
import tr.com.net.common.connection.ARDConnection;
import tr.com.net.common.connection.ConnectionState;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TCP Client Connection implementation by Netty.
 *
 * @param <K>  message type
 * @author mozkeskin
 */
@Log4j2
public class RDTCPClient<K> extends ARDConnection<K> {
    /**
     * Start up delay
     */
    private final static int startUpDelay = 1000; // millisecond
    /**
     * Reconnect delay
     */
    private final static int reconnectDelay = 2000;

    /**
     * Connection timer
     */
    private static final Timer timer = new Timer();

    /**
     * Netty bootstrap
     */
    private Bootstrap bootstrap = new Bootstrap();

    /**
     * Socket address
     */
    private SocketAddress address;

    /**
     * TCP Client constructor
     *
     * @param host server ip
     * @param port server port
     * @param handlers Netty channel handler array, added end of the pipeline in order
     */
    public RDTCPClient(String host, int port, ChannelHandler ... handlers) {
        this.address = new InetSocketAddress(host, port);
        this.handlers = handlers;
    }

    /**
     * Start the TCP Client connection
     */
    public void start() {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(handlers);
                ch.pipeline().addLast(new ConnectionHandler());
            }
        });
        state = ConnectionState.STARTING;
        scheduleConnect(startUpDelay);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(K message) {
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(message);
        }
    }

    /**
     * Handles TCP Client connection and reconnecting to the server when it is up.
     */
    private void doConnect() {
        try {
            if(state != ConnectionState.STARTING)
            {
                stateChanged(ConnectionState.RECONNECTING);
            }
            ChannelFuture f = bootstrap.connect(address);
            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {//if is not successful, reconnect
                        future.channel().close();
                        stateChanged(ConnectionState.RECONNECTING);
                        bootstrap.connect(address).addListener(this);
                    } else {//good, the connection is ok
                        channel = future.channel();
                        //add a listener to detect the connection lost
                        addCloseDetectListener(channel);
                        stateChanged(ConnectionState.CONNECTED);
                        log.info("TCP Client is connected to {}", address);
                    }
                }

                private void addCloseDetectListener(Channel channel) {
                    //if the channel connection is lost, the ChannelFutureListener.operationComplete() will be called
                    channel.closeFuture().addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future)
                                throws Exception {
                            log.warn("TCP Client connection closed");
                            stateChanged(ConnectionState.DISCONNECTED);
                            scheduleConnect(reconnectDelay);
                        }

                    });

                }
            });
        } catch (Exception ex) {
            scheduleConnect(reconnectDelay);

        }
    }

    /**
     * Schedules the connect with given time
     *
     * @param millis delay
     */
    private void scheduleConnect(long millis) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                doConnect();
            }
        }, millis);
    }
}
