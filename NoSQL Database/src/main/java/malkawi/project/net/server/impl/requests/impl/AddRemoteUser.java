package malkawi.project.net.server.impl.requests.impl;

import malkawi.project.database.components.users.User;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.requests.AbstractResponse;
import malkawi.project.net.server.impl.connection.DBClientConnection;
import malkawi.project.utilities.io.console.Console;

public class AddRemoteUser extends AbstractResponse {

    public AddRemoteUser(AbstractConnection connection, Packet requestPacket) {
        super(connection, requestPacket);
    }

    @Override
    public void respond() {
        Console.info("[AddRemoteUser] (ADD_USER) request received.");
        User user = (User) requestPacket.getContent()[0];
        Console.info("[AddRemoteUser] Added user: (" + user.getUsername() + ")");
        ((DBClientConnection) connection).getDatabasesServer().getDatabaseSystem().getUserManager().addUser(user);
    }

    @Override
    public boolean validate() {
        return checkContentLength(1, true) && requestPacket.getContent()[0] instanceof User;
    }

}
