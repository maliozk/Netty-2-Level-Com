package tr.com.net.common.connection;

import io.netty.channel.ChannelHandlerContext;

/**
 * Connection listener to manage to trigger details of the connection and messages.
 *
 * @param <T> message type
 * @author mozkeskin
 */
public interface IRDConnectionListener<T> extends IRDCommonConnectionListener {
    /**
     * Triggered when a new message received
     *
     * @param ctx channel context
     * @param message message
     */
    void messageReceived(ChannelHandlerContext ctx, T message);
}
