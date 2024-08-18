package malkawi.project.net.server.connections;

import lombok.AllArgsConstructor;
import malkawi.project.net.global.connections.pollers.AbstractPoller;
import malkawi.project.net.server.AbstractServer;
import malkawi.project.utilities.io.console.Console;

import java.io.IOException;
import java.net.Socket;

@AllArgsConstructor
public class NewConnectionsPoller extends AbstractPoller {

    private final AbstractServer server;

    @Override
    protected void pollAction() {
        Socket newConnection = acceptConnection();
        if(newConnection != null) {
            Console.success("[NEW CONNECTION] Received connection request from Host: " +
                    newConnection.getInetAddress().getHostName());
            server.addConnection(newConnection);
        }
    }

    private Socket acceptConnection() {
        try {
            return server.getSocket().accept();
        } catch (IOException e) {
            Console.error(
                    "[ERROR] New Connection acceptance failed" +
                            " at NewConnectionsPoller:acceptConnection\n" + e.getMessage());
        }
        return null;
    }

    @Override
    protected boolean validatePolling() {
        return server.isAlive();
    }

}
