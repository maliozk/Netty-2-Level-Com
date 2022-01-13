package tr.com.net.center.connection;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import tr.com.net.common.connection.ConnectionState;
import tr.com.net.common.connection.tuple.IRDTupleConnection;
import tr.com.net.common.connection.tuple.IRDTupleConnectionListener;
import tr.com.net.common.connection.tuple.RDConnectionTuple;
import tr.com.net.message.IMessage;
import tr.com.net.center.CenterConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Center Connection Handler
 */
@Log4j2
public class CenterConnectionHandler {

    private Map<Integer, RDConnectionTuple<IMessage>> startConnectionMap = new HashMap<>();
    private Map<Integer, RDConnectionTuple<IMessage>> endConnectionMap = new HashMap<>();

    private StartListener startListener = new StartListener();
    private EndListener endListener = new EndListener();


    public void addStartConnection(int id, RDConnectionTuple<IMessage> connection) {
        startConnectionMap.put(id, connection);
        connection.addListener(startListener);
    }

    public void addEndConnection(int id, RDConnectionTuple<IMessage> connection) {
        endConnectionMap.put(id, connection);
        connection.addListener(endListener);
    }

    private class StartListener implements IRDTupleConnectionListener<IMessage>
    {

        @Override
        public void stateChanged(ConnectionState oldState, ConnectionState newState) {

        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, IMessage message, IRDTupleConnection.TYPE type, int id) {
            log.info("Message received " + message.getType() + " type : " + type + " id : " + id);
            List<Integer> endList = CenterConfig.startToEnd.get(id);
            if(endList != null)
            {
               endList.forEach(end -> {
                   log.info("Message forwarding to " + message.getType() + " id : " + id + " to " + end);
                   RDConnectionTuple<IMessage> endConnection = endConnectionMap.get(end);
                   if(endConnection != null) {
                       endConnection.sendMessage(type, message);
                   }
               });
            }
        }
    }

    private class EndListener implements IRDTupleConnectionListener<IMessage>
    {

        @Override
        public void stateChanged(ConnectionState oldState, ConnectionState newState) {

        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, IMessage message, IRDTupleConnection.TYPE type, int id) {

        }
    }

}
