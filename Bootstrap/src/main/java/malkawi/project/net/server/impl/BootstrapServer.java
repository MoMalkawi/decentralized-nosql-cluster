package malkawi.project.net.server.impl;

import lombok.Getter;
import lombok.NonNull;
import malkawi.project.data.Config;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.connections.pollers.AbstractPoller;
import malkawi.project.net.server.AbstractServer;
import malkawi.project.net.server.impl.connection.ClientConnection;
import malkawi.project.net.server.impl.poller.ClientPoller;
import malkawi.project.BootstrapInstance;

import java.net.Socket;

@Getter
public class BootstrapServer extends AbstractServer {

    private final BootstrapInstance instance;

    public BootstrapServer(@NonNull int port, BootstrapInstance instance) {
        super(port);
        this.instance = instance;
    }

    public BootstrapServer(BootstrapInstance instance) {
        this(Config.get().getBootstrapPort(), instance);
    }

    @Override
    protected AbstractConnection getConnection(Socket socketConnection) {
        return new ClientConnection(socketConnection, this);
    }

    @Override
    protected AbstractPoller getConnectionPoller(AbstractConnection connection) {
        return new ClientPoller((ClientConnection) connection);
    }

}
