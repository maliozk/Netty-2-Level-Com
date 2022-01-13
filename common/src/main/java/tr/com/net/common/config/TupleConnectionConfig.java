package tr.com.net.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Tuple connection config
 *
 * @author mozkeskin
 */
@AllArgsConstructor
@Getter
@Setter
public class TupleConnectionConfig {
    /**
     * Server IP
     */
    String ip;

    /**
     * TCP Server Port
     */
    int port;

    /**
     * UDP Server Port
     */
    int udpPort;
}
