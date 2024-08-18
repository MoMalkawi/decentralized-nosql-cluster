package malkawi.logging.net.client.impl.bootstrap.connection;

import lombok.Getter;
import malkawi.logging.net.client.impl.bootstrap.BootstrapClient;
import malkawi.logging.net.global.connections.AbstractConnection;

import java.net.Socket;

@Getter
public class BootstrapConnection extends AbstractConnection {

    private final BootstrapClient client;

    public BootstrapConnection(Socket socket, BootstrapClient client) {
        super(socket);
        this.client = client;
    }

}
