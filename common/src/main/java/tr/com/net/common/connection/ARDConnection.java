package tr.com.net.common.connection;

import io.netty.channel.*;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Abstract connection implementation handling the connection details.
 *
 * @param <K> message type
 * @author mozkeskin
 */
@Log4j2
public abstract class ARDConnection<K> implements IRDConnection<K>{

    /**
     * Netty event loop group
     */
    protected EventLoopGroup eventLoopGroup;

    /**
     * Channel created on the connection start
     */
    protected volatile Channel channel;

    /**
     * State of the connection
     */
    protected volatile ConnectionState state;

    /**
     * Handlers to be used in connection initialization. This handlers used in the pipeline as encoder and decoder.
     */
    protected ChannelHandler[] handlers;

    /**
     * Connection listeners to be trigger connection details.
     */
    protected List<IRDConnectionListener<K>> listeners = new CopyOnWriteArrayList<>();

    /**
     * Returns the state of the connection
     *
     * @return connection state
     */
    @Override
    public ConnectionState getState() {
        return state;
    }

    /**
     * Updates the connection state triggers notification
     *
     * @param newState new connection state
     */
    protected void stateChanged(ConnectionState newState) {
        ConnectionState oldState = this.state;
        this.state = newState;
        notifyStateChanged(oldState, newState);
    }

    /**
     * {@inheritDoc}
     */
    public void shutdown() {
        try {
            channel.close().sync();
            eventLoopGroup.shutdownGracefully();
        } catch (InterruptedException e) {
            log.info(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(IRDConnectionListener<K> listener) {
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(IRDConnectionListener<K> listener) {
        listeners.remove(listener);
    }


    /**
     * Called when new channel is active on the connection
     *
     * @param ctx connection context
     */
    protected void conChannelActive(ChannelHandlerContext ctx)
    {
        // Do nothing by default
    }

    /**
     * Called when the channel is changed inactive state on the connection
     *
     * @param ctx connection context
     */
    protected void conChannelInactive(ChannelHandlerContext ctx)
    {
        // Do nothing by default
    }

    /**
     * Notifies the state change on the listener
     *
     * @param oldState old state
     * @param newState new state
     */
    void notifyStateChanged(ConnectionState oldState, ConnectionState newState)
    {
        if(newState != oldState) {
            listeners.forEach(listener -> listener.stateChanged(oldState, newState));
            log.info("Connection state changed from {} to {}", oldState, newState);
        }
    }

    /**
     * Notifies message received on the listener
     *
     * @param ctx connection context
     * @param message message
     */
    void notifyMessageReceived(ChannelHandlerContext ctx, K message)
    {
        listeners.forEach(listener -> listener.messageReceived(ctx, message));
    }

    /**
     * Highest level handler to manage the channel states and the message retrieval
     */
    @ChannelHandler.Sharable
    public class ConnectionHandler extends SimpleChannelInboundHandler<K> {

        /**
         * Triggered by handler in the pipeline when a new message retrieved
         *
         * @param channelHandlerContext connection context
         * @param message new message
         */
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, K message) {
            notifyMessageReceived(channelHandlerContext, message);
        }

        /**
         * Triggered by handler in the pipeline when a new channel is active
         *
         * @param ctx connection context
         * @throws Exception exception
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            conChannelActive(ctx);
        }

        /**
         * Triggered by handler in the pipeline when the channel is inactive
         *
         * @param ctx connection context
         * @throws Exception exception
         */
        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            conChannelInactive(ctx);
        }

        /**
         * Triggered by handler when an exception is occurred
         *
         * @param ctx connection context
         * @param cause exception
         */
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            log.error(cause);
        }
    }
}
