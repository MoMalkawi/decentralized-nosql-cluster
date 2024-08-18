package malkawi.project.net.client.impl;

import lombok.NonNull;
import malkawi.project.net.client.AbstractClient;
import malkawi.project.net.client.impl.connection.NodeConnection;
import malkawi.project.net.client.impl.poller.NodePoller;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.connections.pollers.AbstractPoller;

import java.net.Socket;

public class NodeClient extends AbstractClient {

    public NodeClient(@NonNull String ip, @NonNull int port) {
        super(ip, port);
    }

    @Override
    protected AbstractConnection generateServerConnection(Socket socket) {
        return new NodeConnection(socket, this);
    }

    @Override
    protected AbstractPoller getServerPoller() {
        return new NodePoller(this);
    }

}
