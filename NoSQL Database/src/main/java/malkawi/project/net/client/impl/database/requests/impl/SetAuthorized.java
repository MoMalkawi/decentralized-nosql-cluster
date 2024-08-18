package malkawi.project.net.client.impl.database.requests.impl;

import malkawi.project.database.components.users.User;
import malkawi.project.net.client.impl.database.connections.DBServerConnection;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.requests.AbstractResponse;

public class SetAuthorized extends AbstractResponse {

    public SetAuthorized(AbstractConnection connection, Packet requestPacket) {
        super(connection, requestPacket);
    }

    @Override
    public void respond() {
        ((DBServerConnection) connection).setUserAuthenticated(true);
    }

    @Override
    public boolean validate() {
        return checkContentLength(1, true) && requestPacket.getContent()[0] instanceof User;
    }

}
