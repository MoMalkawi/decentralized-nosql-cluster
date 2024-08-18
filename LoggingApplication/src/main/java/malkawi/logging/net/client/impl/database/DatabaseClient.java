package malkawi.logging.net.client.impl.database;

import lombok.Getter;
import lombok.Setter;
import malkawi.logging.database.data.users.User;
import malkawi.logging.net.client.AbstractClient;
import malkawi.logging.net.client.impl.database.connections.DBServerConnection;
import malkawi.logging.net.client.impl.database.data.ServerInfo;
import malkawi.logging.net.client.impl.database.pollers.DBConnectionPoller;
import malkawi.logging.net.global.connections.AbstractConnection;
import malkawi.logging.net.global.connections.pollers.AbstractPoller;

import java.net.Socket;

@Getter
public class DatabaseClient extends AbstractClient {

    private @Setter User user;

    public DatabaseClient(ServerInfo serverInfo, User user) {
        super(serverInfo.getIP(), serverInfo.getPort());
        this.user = user;
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
