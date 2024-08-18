package malkawi.logging.net.client.impl.database.connections;

import lombok.Getter;
import lombok.Setter;
import malkawi.logging.database.data.result.Result;
import malkawi.logging.net.client.impl.database.DatabaseClient;
import malkawi.logging.net.global.connections.AbstractConnection;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Getter
public class DBServerConnection extends AbstractConnection {

    private final DatabaseClient client;

    private @Setter boolean userAuthenticated;

    private @Setter @Getter String loggedDatabase;

    private final @Getter Map<Long, Result> resultsCache = new HashMap<>();

    public DBServerConnection(Socket socket, DatabaseClient client) {
        super(socket);
        this.client = client;
    }

}
