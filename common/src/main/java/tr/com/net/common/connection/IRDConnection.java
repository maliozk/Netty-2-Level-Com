package tr.com.net.common.connection;

/**
 * Connection interface to be used in connection implementation having single connection
 *
 * @param <T> message type
 * @author mozkeskin
 */
public interface IRDConnection<T> extends IRDCommonConnection<IRDConnectionListener<T>>{
    /**
     * Sends message over defined connection
     *
     * @param message message
     */
    void sendMessage(T message);
}
