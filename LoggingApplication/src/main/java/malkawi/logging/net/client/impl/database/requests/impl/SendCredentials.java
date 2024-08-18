package malkawi.logging.net.client.impl.database.requests.impl;

import malkawi.logging.database.data.users.User;
import malkawi.logging.net.client.impl.database.connections.DBServerConnection;
import malkawi.logging.net.global.connections.AbstractConnection;
import malkawi.logging.net.global.data.Packet;
import malkawi.logging.net.global.data.PacketBuilder;
import malkawi.logging.net.global.requests.AbstractResponse;

public class SendCredentials extends AbstractResponse {

    private static final User EMPTY_USER = new User(-1, "", "", User.Role.USER);

    public SendCredentials(AbstractConnection connection, Packet requestPacket) {
        super(connection, requestPacket);
    }

    @Override
    public void respond() {
        User user = getUser();
        user.setRole(User.Role.USER);
        connection.send(new PacketBuilder().typeName("ACK").values(user).build());
    }

    private User getUser() {
        User user = ((DBServerConnection) connection).getClient().getUser();
        return user != null ? user : EMPTY_USER;
    }

    @Override
    public boolean validate() {
        return checkContentLength(0, false);
    }

}
