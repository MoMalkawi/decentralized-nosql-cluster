package malkawi.project.net.server.impl.requests.impl;

import malkawi.project.database.data.Result;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.data.PacketBuilder;
import malkawi.project.net.global.requests.AbstractResponse;
import malkawi.project.net.server.impl.connection.DBClientConnection;

public class OperateDatabase extends AbstractResponse {

    private static final PacketBuilder FAILED_OPERATION = new PacketBuilder()
            .typeName("API_RESPONSE").values(Result.FAILURE);

    public OperateDatabase(AbstractConnection connection, Packet requestPacket) {
        super(connection, requestPacket);
    }

    @Override
    public void respond() {
        Result result = getResult();
        connection.send(
                result != null ?
                        new PacketBuilder().typeName("API_RESPONSE").values(result)
                                .identifier(requestPacket.getIdentifier()).build()
                        :
                        FAILED_OPERATION.identifier(requestPacket.getIdentifier()).build());
    }

    private Result getResult() {
        return ((DBClientConnection) connection).getSession().getApi()
                .executeAPICall(requestPacket.getContent());
    }

    @Override
    public boolean validate() {
        return checkContentLength(1, false);
    }

}
