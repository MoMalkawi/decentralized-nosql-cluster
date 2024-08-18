package malkawi.project.net.client;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.connections.pollers.AbstractPoller;
import malkawi.project.utilities.Sleep;
import malkawi.project.utilities.io.console.Console;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public abstract class AbstractClient {

    private ExecutorService singlePool;

    private final @Getter @NonNull String ip;

    private final @Getter @NonNull int port;

    private @Getter AbstractConnection connection;

    private AbstractPoller poller;

    public void start() {
        if(isAlive())
            return;
        try {
            startSocket();
            startPool();
            startPoller();
        } catch (IOException e) {
            Console.error(
                    "[FATAL] Error connecting to client [" + ip + ":" + port + "].\n" + e.getMessage());
        }
    }

    private void startSocket() throws IOException {
        Socket socket = new Socket(ip, port);
        connection = generateServerConnection(socket);
    }

    private void startPool() {
        if(singlePool == null || singlePool.isShutdown() || singlePool.isTerminated())
            singlePool = Executors.newSingleThreadExecutor();
    }

    @SuppressWarnings("all")
    private void startPoller() {
        poller = getServerPoller();
        singlePool.submit(poller);
    }

    protected abstract AbstractConnection generateServerConnection(Socket socket);

    protected abstract AbstractPoller getServerPoller();

    public void stop() {
        stopPoller();
        stopConnection();
    }

    private void stopPoller() {
        if(poller != null)
            poller.setEnabled(false);
        if(singlePool != null && !singlePool.isShutdown()) {
            singlePool.shutdown();
            Sleep.sleepUntil(singlePool::isShutdown, 1500, 10);
        }
    }

    private void stopConnection() {
        if(isAlive())
            connection.terminate();
    }

    public boolean isAlive() {
        return connection != null && connection.isAlive();
    }

}
