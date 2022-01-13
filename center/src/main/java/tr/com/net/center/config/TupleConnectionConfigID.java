package tr.com.net.center.config;

import lombok.Getter;
import tr.com.net.common.config.TupleConnectionConfig;

public class TupleConnectionConfigID extends TupleConnectionConfig {
    @Getter
    int id;

    public TupleConnectionConfigID(int id, String ip, int port, int udpPort) {
        super(ip, port, udpPort);
        this.id = id;
    }
}
