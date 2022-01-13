package tr.com.net.common.connection;

/**
 * Common connection listener to manage to trigger details of the connection and messages.
 *
 * @author mozkeskin
 */
public interface IRDCommonConnectionListener {
    /**
     * Triggered when the state of the connection is changed
     *
     * @param oldState old state of the connection
     * @param newState new state of the connection
     */
    void stateChanged(ConnectionState oldState, ConnectionState newState);
}
