package malkawi.project.net.server;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.connections.pollers.AbstractPoller;
import malkawi.project.net.server.connections.NewConnectionsPoller;
import malkawi.project.utilities.io.console.Console;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public abstract class AbstractServer implements AutoCloseable {

    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    private final List<AbstractConnection> connections = new ArrayList<>();

    private final @Getter @NonNull int port;

    private @Getter ServerSocket socket;

    private AbstractPoller newConnectionsPoller;

    public void start() {
        if(isAlive())
            return;
        try {
            socket = new ServerSocket(port);
            startPoller();
            Console.success("[Server] started at port (" + port + ")");
        } catch (IOException e) {
            Console.error("[FATAL] can't start server socket at AbstractServer:start()\n" + e.getMessage());
        }
    }

    private void startPoller() {
        newConnectionsPoller = new NewConnectionsPoller(this);
        threadPool.submit(newConnectionsPoller);
    }

    public void addConnection(Socket socketConnection) {
        AbstractConnection connection = getConnection(socketConnection);
        threadPool.submit(getConnectionPoller(connection));
        connections.add(connection);
    }

    protected abstract AbstractConnection getConnection(Socket socketConnection);

    protected abstract AbstractPoller getConnectionPoller(AbstractConnection connection);

    public void stop() {
        stopPoller();
        stopConnections();
        stopServer();
    }

    private void stopPoller() {
        if(newConnectionsPoller != null)
            newConnectionsPoller.setEnabled(false);
    }

    private void stopConnections() {
        connections.forEach(AbstractConnection::terminate);
    }

    private void stopServer() {
        if(isAlive()) {
            try {
                socket.close();
            } catch (IOException e) {
                Console.error(
                        "[ERROR] Can't close server at Server:stopServer.\n" + e.getMessage());
            }
        }
    }

    public boolean isAlive() {
        return socket != null && !socket.isClosed();
    }

    @Override
    public void close() {
        stop();
    }

}
