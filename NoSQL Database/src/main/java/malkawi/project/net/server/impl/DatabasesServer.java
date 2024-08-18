package malkawi.project.net.server.impl;

import lombok.Getter;
import malkawi.project.database.DBSystem;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.connections.pollers.AbstractPoller;
import malkawi.project.net.server.AbstractServer;
import malkawi.project.net.server.impl.connection.DBClientConnection;
import malkawi.project.net.server.impl.pollers.DatabaseClientPoller;

import java.net.Socket;

public class DatabasesServer extends AbstractServer {

    private final @Getter DBSystem databaseSystem;

    public DatabasesServer(int port) {
        super(port);
        this.databaseSystem = new DBSystem();
    }

    @Override
    protected AbstractConnection getConnection(Socket socketConnection) {
        return new DBClientConnection(socketConnection, this);
    }

    @Override
    protected AbstractPoller getConnectionPoller(AbstractConnection connection) {
        return new DatabaseClientPoller((DBClientConnection) connection);
    }

}
