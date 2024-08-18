package malkawi.logging.net.client.impl.database.requests.impl;

import malkawi.logging.database.data.result.Result;
import malkawi.logging.net.client.impl.database.connections.DBServerConnection;
import malkawi.logging.net.global.connections.AbstractConnection;
import malkawi.logging.net.global.data.Packet;
import malkawi.logging.net.global.requests.AbstractResponse;
import malkawi.logging.utilities.io.console.Console;

public class RegisterDatabase extends AbstractResponse {

    public RegisterDatabase(AbstractConnection connection, Packet requestPacket) {
        super(connection, requestPacket);
    }

    @Override
    public void respond() {
        String databaseName = ((Result)requestPacket.getContent()[0]).next().getString(1);
        ((DBServerConnection) connection).setLoggedDatabase(databaseName);
        Console.success("[DatabaseSystem] Successfully logged into (" + databaseName + ") database.");
    }

    @Override
    public boolean validate() {
        return checkContentLength(1, true);
    }

}
