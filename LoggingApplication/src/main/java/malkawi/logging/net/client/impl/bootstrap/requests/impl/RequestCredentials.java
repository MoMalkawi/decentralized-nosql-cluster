package malkawi.logging.net.client.impl.bootstrap.requests.impl;

import malkawi.logging.database.data.users.User;
import malkawi.logging.net.client.impl.bootstrap.connection.BootstrapConnection;
import malkawi.logging.net.global.connections.AbstractConnection;
import malkawi.logging.net.global.data.Packet;
import malkawi.logging.net.global.data.PacketBuilder;
import malkawi.logging.net.global.requests.AbstractResponse;
import malkawi.logging.utilities.io.console.Console;

public class RequestCredentials extends AbstractResponse {

    public RequestCredentials(AbstractConnection connection, Packet requestPacket) {
        super(connection, requestPacket);
    }

    @Override
    public void respond() {
        User existingUser = ((BootstrapConnection) connection).getClient().getService().getUser();
        connection.send(new PacketBuilder().typeName("ACK").values(existingUser != null ? existingUser : "").build());
        Console.info("[RequestCredentials] Sent a credentials request from BootStrap Client.");
    }

    @Override
    public boolean validate() {
        return checkContentLength(0, false);
    }

}
