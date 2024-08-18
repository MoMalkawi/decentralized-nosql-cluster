package malkawi.project.net.server.impl.connection;

import lombok.Getter;
import lombok.Setter;
import malkawi.project.data.profiles.User;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.server.impl.BootstrapServer;

import java.net.Socket;

@Getter
public class ClientConnection extends AbstractConnection {

    private final BootstrapServer server;

    private @Setter User loggedUser;

    public ClientConnection(Socket socket, BootstrapServer server) {
        super(socket);
        this.server = server;
    }

}
