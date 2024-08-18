package malkawi.logging.net.global.requests.impl;

import malkawi.logging.net.global.connections.AbstractConnection;
import malkawi.logging.net.global.data.Packet;
import malkawi.logging.net.global.data.PacketBuilder;
import malkawi.logging.net.global.requests.AbstractResponse;

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
