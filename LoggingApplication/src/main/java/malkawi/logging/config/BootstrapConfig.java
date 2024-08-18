package malkawi.logging.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "bootstrap")
@Getter @Setter
@Component
public class BootstrapConfig {

    private String ip;

    private int port;

}
