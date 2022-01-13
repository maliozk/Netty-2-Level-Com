package tr.com.net.common.connection.tuple;

import tr.com.net.common.connection.IRDCommonConnection;

/**
 * Connection tuple interface to be used in connection implementation having more than two connection.
 * The connection is controlled over this interface in order to start, stop and
 * getting messages and states via the connection listener.
 *
 * @author mozkeskin
 */
public interface IRDTupleConnection<T> extends IRDCommonConnection<IRDTupleConnectionListener<T>> {
    /**
     * Connection Type Enumeration
     */
    enum TYPE {UDP, TCP}

    /**
     * Sends the message with given type
     *
     * @param type message type to be sent
     * @param message message
     */
    void sendMessage(TYPE type, T message);
}
