package tr.com.net.start.connection;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import tr.com.net.common.connection.IRDConnection;
import tr.com.net.common.connection.IRDConnectionListener;
import tr.com.net.common.connection.ConnectionState;
import tr.com.net.common.connection.tuple.IRDTupleConnection;
import tr.com.net.common.connection.tuple.RDConnectionTuple;
import tr.com.net.message.IMessage;

/**
 * Start Connection Handler
 */
@Log4j2
public class StartConnectionHandler implements IRDConnectionListener<IMessage> {

    private IRDConnection<IMessage> startConnection;
    private RDConnectionTuple<IMessage> centerconnection1;
    private RDConnectionTuple<IMessage> centerConnection2;


    public StartConnectionHandler(IRDConnection<IMessage> startConnection, RDConnectionTuple<IMessage> centerConnection1, RDConnectionTuple<IMessage> centerConnection2) {
        this.startConnection = startConnection;
        this.centerconnection1 = centerConnection1;
        this.centerConnection2 = centerConnection2;

        this.startConnection.addListener(this);
    }

    public ConnectionState getCenterState1()
    {
        return centerconnection1.getState();
    }

    public ConnectionState getCenterState2()
    {
        return centerConnection2.getState();
    }

    public ConnectionState getRadarState()
    {
        return startConnection.getState();
    }

    @Override
    public void stateChanged(ConnectionState oldState, ConnectionState newState) {

    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, IMessage message) {
        IRDTupleConnection.TYPE type = message.getType() == 1 ? IRDTupleConnection.TYPE.TCP : IRDTupleConnection.TYPE.UDP;

        log.info("Message received " + message.getType() + ". Sending to Centers. Start: " + getRadarState() + " 1: " + getCenterState1() + " 2: " + getCenterState2());

        centerconnection1.sendMessage(type, message);
    }
}
