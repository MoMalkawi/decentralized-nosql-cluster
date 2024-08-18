package malkawi.logging.net.global.requests.impl;

import malkawi.logging.net.global.connections.AbstractConnection;
import malkawi.logging.net.global.data.Packet;
import malkawi.logging.net.global.requests.AbstractResponse;

public class Terminate extends AbstractResponse {

    public Terminate(AbstractConnection connection, Packet requestPacket) {
        super(connection, requestPacket);
    }

    @Override
    public void respond() {
        connection.terminate();
    }

    @Override
    public boolean validate() {
        return true;
    }

}
