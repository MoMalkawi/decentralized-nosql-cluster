package malkawi.project.net.client.impl.database;

import lombok.Getter;
import malkawi.project.net.client.impl.database.connections.DBServerConnection;
import malkawi.project.net.client.impl.database.data.DatabaseServerInfo;
import malkawi.project.net.client.impl.database.pollers.DBConnectionPoller;
import malkawi.project.net.client.AbstractClient;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.connections.pollers.AbstractPoller;

import java.net.Socket;

@Getter
public class DatabaseClient extends AbstractClient {

    public DatabaseClient(DatabaseServerInfo serverInfo) {
        super(serverInfo.getIP(), serverInfo.getPort());
    }

    @Override
    protected AbstractConnection generateServerConnection(Socket socket) {
        return new DBServerConnection(socket, this);
    }

    @Override
    protected AbstractPoller getServerPoller() {
        return new DBConnectionPoller(this);
    }

    public DBServerConnection getDatabaseConnection() {
        return (DBServerConnection) getConnection();
    }

    @Override
    public void stop() {
        super.stop();
        if(getConnection() != null)
            ((DBServerConnection) getConnection()).setUserAuthenticated(false);
    }

}
