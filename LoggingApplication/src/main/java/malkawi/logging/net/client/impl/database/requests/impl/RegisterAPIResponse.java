package malkawi.logging.net.client.impl.database.requests.impl;

import malkawi.logging.database.data.result.Result;
import malkawi.logging.net.client.impl.database.connections.DBServerConnection;
import malkawi.logging.net.global.connections.AbstractConnection;
import malkawi.logging.net.global.data.Packet;
import malkawi.logging.net.global.requests.AbstractResponse;

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
