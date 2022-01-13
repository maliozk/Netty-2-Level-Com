package tr.com.net.common.connection.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.log4j.Log4j2;
import tr.com.net.common.connection.ARDConnection;
import tr.com.net.common.connection.ConnectionState;

/**
 * TCP Server Connection implementation by Netty.
 *
 * @param <T>  message type
 * @author mozkeskin
 */
@Log4j2
public class RDTCPServer<T> extends ARDConnection<T> {
    /**
     * Server port
     */
    private int port;

    /**
     * Channel group connected to the server
     */
    private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * TCP Server constructor
     *
     * @param port server port
     * @param handlers Netty channel handler array, added end of the pipeline in order
     */
    public RDTCPServer(int port, ChannelHandler... handlers) {
        this.port = port;
        this.handlers = handlers;
    }

    /**
     * Start the TCP Server connection
     */
    public synchronized void start() {

        eventLoopGroup = new NioEventLoopGroup(2);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(eventLoopGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(handlers);
                    pipeline.addLast(new ConnectionHandler());
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            log.info("Server is open at {}", port);

            channel = channelFuture.channel();
            stateChanged(ConnectionState.CONNECTED);
        } catch (Exception e) {
            log.error(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(T message) {
        channelGroup.writeAndFlush(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void conChannelActive(ChannelHandlerContext ctx) {
        channelGroup.add(ctx.channel());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void conChannelInactive(ChannelHandlerContext ctx) {
        channelGroup.remove(ctx.channel());
    }
}
