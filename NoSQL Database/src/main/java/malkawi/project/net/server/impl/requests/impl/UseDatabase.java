package malkawi.project.net.server.impl.requests.impl;

import malkawi.project.database.components.databases.Database;
import malkawi.project.database.data.Result;
import malkawi.project.database.data.ResultBuilder;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.data.PacketBuilder;
import malkawi.project.net.global.requests.AbstractResponse;
import malkawi.project.net.server.impl.connection.DBClientConnection;
import malkawi.project.utilities.io.console.Console;

public class UseDatabase extends AbstractResponse {

    public UseDatabase(AbstractConnection connection, Packet requestPacket) {
        super(connection, requestPacket);
    }

    @Override
    public void respond() {
        loginDatabase();
        Result result = getLoggedResult();
        if(result.isSuccess()) {
            Console.success("[System] Verified a database access request.");
            connection.send(new PacketBuilder()
                    .typeName("LOGGED_DATABASE")
                    .values(result)
                    .build());
        }
    }

    private void loginDatabase() {
        String databaseName = (String) requestPacket.getContent()[0];
        Console.info("[System] Verifying existence & client access permissions of database (" + databaseName + ").");
        ((DBClientConnection) connection).getSession().databaseLogin(databaseName);
    }

    private Result getLoggedResult() {
        Database loggedDatabase = ((DBClientConnection) connection).getSession().getDatabase();
        return loggedDatabase != null ?
                new ResultBuilder()
                        .success()
                        .message("You have logged into [" + loggedDatabase.getName() + "]")
                        .row(loggedDatabase.getId(), loggedDatabase.getName())
                        .build()
                :
                Result.FAILURE;
    }

    @Override
    public boolean validate() {
        return checkContentLength(1, true) && requestPacket.getContent()[0] instanceof String;
    }

}
