package malkawi.project.net.client.impl.database.data;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@AllArgsConstructor
@ToString
public class DatabaseServerInfo {

    private final String ip;

    private final int port;

    public String getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatabaseServerInfo that = (DatabaseServerInfo) o;
        return port == that.port && ip.equals(that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}
