package malkawi.logging.net.client.impl.database.pollers;

import lombok.AllArgsConstructor;
import malkawi.logging.net.client.impl.database.DatabaseClient;
import malkawi.logging.net.client.impl.database.requests.DBResponseFactory;
import malkawi.logging.net.global.connections.pollers.AbstractPoller;
import malkawi.logging.net.global.data.Packet;
import malkawi.logging.net.global.requests.AbstractResponse;

@AllArgsConstructor
public class DBConnectionPoller extends AbstractPoller {

    private final DatabaseClient client;

    @Override
    protected void pollAction() {
        Packet packet = client.getConnection().receivePacket();
        replyTo(packet);
    }

    public void replyTo(Packet packet) {
        if(packet != null) {
            client.getConnection().setLastReceivedPacket(System.currentTimeMillis());
            AbstractResponse response = DBResponseFactory
                    .getResponse(client.getConnection(), packet);
            if(response != null && response.validate())
                response.respond();
        }
    }

    @Override
    protected boolean validatePolling() {
        return client.getConnection().isAlive();
    }

}
