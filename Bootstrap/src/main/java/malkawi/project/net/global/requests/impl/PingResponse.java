package malkawi.project.net.global.requests.impl;

import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.data.PacketBuilder;
import malkawi.project.net.global.requests.AbstractResponse;

public class PingResponse extends AbstractResponse {

    public PingResponse(AbstractConnection connection, Packet requestPacket) {
        super(connection, requestPacket);
    }

    @Override
    public void respond() {
        connection.send(new PacketBuilder().typeName("EMPTY").build());
    }

    @Override
    public boolean validate() {
        return checkContentLength(0, false);
    }

}
