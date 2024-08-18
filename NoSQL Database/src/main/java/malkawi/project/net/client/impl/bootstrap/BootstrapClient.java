package malkawi.project.net.client.impl.bootstrap;

import lombok.Getter;
import malkawi.project.DatabaseNode;
import malkawi.project.data.Config;
import malkawi.project.net.client.AbstractClient;
import malkawi.project.net.client.impl.bootstrap.connection.BootstrapConnection;
import malkawi.project.net.client.impl.bootstrap.pollers.BootstrapPoller;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.connections.pollers.AbstractPoller;

import java.net.Socket;

@Getter
public class BootstrapClient extends AbstractClient {

    private final DatabaseNode node;

    public BootstrapClient(DatabaseNode node) {
        super(Config.get().getBootstrapHost(), Config.get().getBootStrapPort());
        this.node = node;
    }

    @Override
    protected AbstractConnection generateServerConnection(Socket socket) {
        return new BootstrapConnection(socket, this);
    }

    @Override
    protected AbstractPoller getServerPoller() {
        return new BootstrapPoller(this);
    }

}
