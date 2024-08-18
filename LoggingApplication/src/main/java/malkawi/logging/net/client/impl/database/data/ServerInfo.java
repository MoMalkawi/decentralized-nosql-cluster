package malkawi.logging.net.client.impl.database.data;

import lombok.AllArgsConstructor;

@AllArgsConstructor
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
