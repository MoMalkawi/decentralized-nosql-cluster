package malkawi.project.net.client.impl.requests.impl;

import malkawi.project.data.profiles.User;
import malkawi.project.net.client.impl.connection.NodeConnection;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.requests.AbstractResponse;

public class SetAuthorized extends AbstractResponse {

    public SetAuthorized(AbstractConnection connection, Packet requestPacket) {
        super(connection, requestPacket);
    }

    @Override
    public void respond() {
        ((NodeConnection) connection).setUserAuthenticated(true);
    }

    @Override
    public boolean validate() {
        return checkContentLength(1, true) && requestPacket.getContent()[0] instanceof User;
    }

}
