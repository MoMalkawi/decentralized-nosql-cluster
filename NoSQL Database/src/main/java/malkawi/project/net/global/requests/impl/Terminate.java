package malkawi.project.net.global.requests.impl;

import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.requests.AbstractResponse;

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
