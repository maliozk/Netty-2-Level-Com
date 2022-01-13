package tr.com.net.common.connection.tuple;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import tr.com.net.common.connection.IRDConnection;
import tr.com.net.common.connection.IRDConnectionListener;
import tr.com.net.common.connection.ConnectionState;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Connection Tuple implementation. This class holds a tcp and an udp connection, messages sending and receiving
 * for each connection is handled by this class.
 *
 * @param <T>  message type
 * @author mozkeskin
 */
@Log4j2
public class RDConnectionTuple<T> implements IRDTupleConnection<T> {

    /**
     * TCP connection
     */
    private IRDConnection<T> tcpConnection;

    /**
     * UDP connection
     */
    private IRDConnection<T> udpConnection;

    /**
     * Unit id
     */
    private int id;

    /**
     * Connection state, combination of two connection
     */
    private ConnectionState state;

    /**
     * State of tcp connection
     */
    private ConnectionState stateTCP;

    /**
     * State of the udp connection
     */
    private ConnectionState stateUDP;

    /**
     * Listener list
     */
    protected List<IRDTupleConnectionListener<T>> listeners = new CopyOnWriteArrayList<>();

    /**
     * Tuple connection constructor
     *
     * @param id unit id
     * @param tcpConnection tcp connection
     * @param udpConnection udp connection
     */
    public RDConnectionTuple(int id, IRDConnection<T> tcpConnection, IRDConnection<T> udpConnection) {
        this.id = id;
        tcpConnection.addListener(new TCPConnectionListener());
        this.tcpConnection = tcpConnection;
        udpConnection.addListener(new UDPConnectionListener());
        this.udpConnection = udpConnection;

    }

    /**
     * Add a new tuple connection listener
     *
     * @param listener connection listener
     */
    public void addListener(IRDTupleConnectionListener<T> listener) {
        listeners.add(listener);
    }

    /**
     * Removes the connection listener
     *
     * @param listener connection listener
     */
    public void removeListener(IRDTupleConnectionListener<T> listener) {
        listeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectionState getState() {
        return state;
    }

    /**
     * Starts both connection
     */
    @Override
    public void start() {
        tcpConnection.start();
        udpConnection.start();
    }

    /**
     * Shuts down both connection
     */
    @Override
    public void shutdown() {
        tcpConnection.shutdown();
        udpConnection.shutdown();
    }


    /**
     * Send the message according to given connection type
     *
     * @param type    message type to be sent
     * @param message message
     */
    @Override
    public void sendMessage(TYPE type, T message) {
        if(type == TYPE.UDP)
        {
            udpConnection.sendMessage(message);
        }
        else if(type == TYPE.TCP)
        {
            tcpConnection.sendMessage(message);
        }
    }

    /**
     * Updates the state and notify the listeners
     *
     * @param tcpState tcp connection state
     * @param udpState udp connection state
     */
    private void updateNotifyState(ConnectionState tcpState, ConnectionState udpState)
    {
        ConnectionState newState = ConnectionState.DISCONNECTED;
        if(tcpState == ConnectionState.CONNECTED && udpState == ConnectionState.CONNECTED)
        {
            newState = ConnectionState.CONNECTED;
        }
        ConnectionState oldState = state;
        state = newState;
        if(newState != oldState) {
            listeners.forEach(listener -> listener.stateChanged(oldState, state));
            log.info("Connection state changed from {} to {}", oldState, newState);
        }
    }

    /**
     * Notifies the message received on the listeners
     *
     * @param ctx channel context
     * @param message message
     * @param type type
     */
    private void notifyMessageReceived(ChannelHandlerContext ctx, T message, TYPE type)
    {
        listeners.forEach(listener -> listener.messageReceived(ctx, message, type, id));
    }


    /**
     * TCP connection listener
     */
    class TCPConnectionListener implements IRDConnectionListener<T>
    {

        /**
         * {@inheritDoc}
         */
        @Override
        public void stateChanged(ConnectionState oldState, ConnectionState newState) {
            stateTCP = newState;
            updateNotifyState(stateTCP, stateUDP);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void messageReceived(ChannelHandlerContext ctx, T message) {
            notifyMessageReceived(ctx, message, TYPE.TCP);
        }
    }

    /**
     * UDP connection listener
     */
    class UDPConnectionListener implements IRDConnectionListener<T>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void stateChanged(ConnectionState oldState, ConnectionState newState) {
            stateUDP = newState;
            updateNotifyState(stateTCP, stateUDP);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void messageReceived(ChannelHandlerContext ctx, T message) {
            notifyMessageReceived(ctx, message, TYPE.UDP);
        }
    }
}
