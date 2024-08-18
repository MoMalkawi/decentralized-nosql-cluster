package malkawi.logging.net.client.impl.bootstrap;

import lombok.Getter;
import malkawi.logging.net.client.AbstractClient;
import malkawi.logging.net.client.impl.bootstrap.connection.BootstrapConnection;
import malkawi.logging.net.client.impl.bootstrap.pollers.BootstrapPoller;
import malkawi.logging.net.global.connections.AbstractConnection;
import malkawi.logging.net.global.connections.pollers.AbstractPoller;
import malkawi.logging.service.BootstrapService;

import java.net.Socket;

@Getter
public class BootstrapClient extends AbstractClient {

    private final BootstrapService service;

    public BootstrapClient(BootstrapService service) {
        super(service.getBootstrapServerInfo().getIP(), service.getBootstrapServerInfo().getPort());
        this.service = service;
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
