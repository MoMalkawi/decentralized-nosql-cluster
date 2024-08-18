package malkawi.project.net.global.data;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ServerInfo {

    private final String ip;

    private final int port;

    public String getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
