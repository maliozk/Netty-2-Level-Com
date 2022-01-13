package tr.com.net.common.connection.tuple;

import io.netty.channel.ChannelHandlerContext;
import tr.com.net.common.connection.IRDCommonConnectionListener;

/**
 * Tuple connection listener to manage to trigger details of the connection and messages.
 *
 * @author mozkeskin
 */
public interface IRDTupleConnectionListener<T> extends IRDCommonConnectionListener {

    /**
     * Triggered when a new message received
     *
     * @param ctx channel context
     * @param message message
     * @param type connection type
     * @param id connection id
     */
    void messageReceived(ChannelHandlerContext ctx, T message, IRDTupleConnection.TYPE type, int id);
}
