package malkawi.project.net.client.impl.database.requests.impl;


import malkawi.project.database.data.Result;
import malkawi.project.net.client.impl.database.connections.DBServerConnection;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.requests.AbstractResponse;

public class RegisterAPIResponse extends AbstractResponse {

    public RegisterAPIResponse(AbstractConnection connection, Packet requestPacket) {
        super(connection, requestPacket);
    }

    @Override
    public void respond() {
        ((DBServerConnection) connection).getResultsCache().put(
                requestPacket.getIdentifier(),
                (Result) requestPacket.getContent()[0]);
    }

    @Override
    public boolean validate() {
        return checkContentLength(1, true) && requestPacket.getContent()[0] instanceof Result;
    }

}
