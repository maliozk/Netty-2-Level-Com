package tr.com.net.start;

import tr.com.net.common.config.RDConnectionConfig;
import tr.com.net.common.config.TupleConnectionConfig;

public class StartConfig {
    public static RDConnectionConfig simConfig = new RDConnectionConfig("127.0.0.1", 5100);
    public static TupleConnectionConfig centerConfig1 = new TupleConnectionConfig("127.0.0.1", 5110, 5111);
    public static TupleConnectionConfig centerConfig2 = new TupleConnectionConfig("127.0.0.1", 5120, 5121);
}
