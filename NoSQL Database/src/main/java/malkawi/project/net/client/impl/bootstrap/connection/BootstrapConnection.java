package malkawi.project.net.client.impl.bootstrap.connection;

import lombok.Getter;
import lombok.Setter;
import malkawi.project.net.client.impl.bootstrap.BootstrapClient;
import malkawi.project.net.global.connections.AbstractConnection;

import java.net.Socket;

@Getter
public class BootstrapConnection extends AbstractConnection {

    private final BootstrapClient client;

    private @Setter boolean successfulPopulation;

    public BootstrapConnection(Socket socket, BootstrapClient client) {
        super(socket);
        this.client = client;
    }

}
