package tr.com.net.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Connection config
 *
 * @author mozkeskin
 */
@AllArgsConstructor
@Getter
@Setter
public class RDConnectionConfig {
    /**
     * Server IP
     */
    String ip;

    /**
     * Server Port
     */
    int port;
}
