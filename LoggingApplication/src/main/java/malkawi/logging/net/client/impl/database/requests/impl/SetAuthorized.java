package malkawi.logging.net.client.impl.database.requests.impl;

import malkawi.logging.database.data.users.User;
import malkawi.logging.net.client.impl.database.connections.DBServerConnection;
import malkawi.logging.net.global.connections.AbstractConnection;
import malkawi.logging.net.global.data.Packet;
import malkawi.logging.net.global.requests.AbstractResponse;

public class SetAuthorized extends AbstractResponse {

    public SetAuthorized(AbstractConnection connection, Packet requestPacket) {
        super(connection, requestPacket);
    }

    @Override
    public void respond() {
        setUser();
        ((DBServerConnection) connection).setUserAuthenticated(true);
    }

    private void setUser() {
        if(((DBServerConnection) connection).getClient().getUser().getId() == -1)
            ((DBServerConnection) connection).getClient().setUser((User) requestPacket.getContent()[0]);
    }

    @Override
    public boolean validate() {
        return checkContentLength(1, true) && requestPacket.getContent()[0] instanceof User;
    }

}
