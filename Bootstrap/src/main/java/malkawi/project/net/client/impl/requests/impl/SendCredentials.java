package malkawi.project.net.client.impl.requests.impl;

import malkawi.project.data.profiles.User;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.data.PacketBuilder;
import malkawi.project.net.global.requests.AbstractResponse;

public class SendCredentials extends AbstractResponse {

    private static final User ROOT = new User(-1, "root", "root", User.Role.ROOT);

    public SendCredentials(AbstractConnection connection, Packet requestPacket) {
        super(connection, requestPacket);
    }

    @Override
    public void respond() {
        connection.send(new PacketBuilder().typeName("ACK").values(ROOT).build());
    }

    @Override
    public boolean validate() {
        return checkContentLength(0, false);
    }

}
