package tr.com.net.common.connection;

/**
 * Common connection interface to be used in connection implementation.
 * The connection is controlled over this interface in order to start, stop and
 * getting messages and states via the connection listener.
 *
 * @author mozkeskin
 */
public interface IRDCommonConnection<K extends IRDCommonConnectionListener> {
    /**
     * Returns the state of the connection {@see RDConnectionState}
     *
     * @return the state of the connection
     */
    ConnectionState getState();

    /**
     * Starts the connection.
     */
    void start();

    /**
     * Shuts down the connection.
     */
    void shutdown();

    /**
     * Adds listener to be triggered on connection state change and message retrieval
     *
     * @param listener connection listener
     */
    void addListener(K listener);

    /**
     * Removes the listener
     *
     * @param listener connection listener
     */
    void removeListener(K listener);
}
