package malkawi.project.net.server.impl.requests.impl;

import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.data.PacketBuilder;
import malkawi.project.net.global.requests.AbstractResponse;

public class AcknowledgeSYN extends AbstractResponse {

    public AcknowledgeSYN(AbstractConnection abstractConnection, Packet requestData) {
        super(abstractConnection, requestData);
    }

    @Override
    public void respond() {
        connection.send(new PacketBuilder().typeName("SYN_ACK").build());
    }

    @Override
    public boolean validate() {
        return checkContentLength(0, false);
    }

}
