package malkawi.project.net.client.impl.connection;

import lombok.Getter;
import lombok.Setter;
import malkawi.project.net.client.impl.NodeClient;
import malkawi.project.net.global.connections.AbstractConnection;

import java.net.Socket;

@Getter
public class NodeConnection extends AbstractConnection {

    private final NodeClient client;

    private @Setter boolean userAuthenticated;

    public NodeConnection(Socket socket, NodeClient client) {
        super(socket);
        this.client = client;
    }

}
