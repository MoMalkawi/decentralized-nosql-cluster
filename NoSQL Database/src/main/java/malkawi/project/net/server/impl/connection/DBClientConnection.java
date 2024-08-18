package malkawi.project.net.server.impl.connection;

import lombok.Getter;
import malkawi.project.database.DBSystemSession;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.server.impl.DatabasesServer;

import java.net.Socket;

@Getter
public class DBClientConnection extends AbstractConnection {

    private final DBSystemSession session;

    private final DatabasesServer databasesServer;

    public DBClientConnection(Socket socket, DatabasesServer databasesServer) {
        super(socket);
        this.databasesServer = databasesServer;
        this.session = new DBSystemSession(databasesServer.getDatabaseSystem());
    }

}
