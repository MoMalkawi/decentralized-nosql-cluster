package malkawi.logging.net.client.impl.bootstrap.pollers;

import lombok.AllArgsConstructor;
import malkawi.logging.net.client.impl.bootstrap.BootstrapClient;
import malkawi.logging.net.client.impl.bootstrap.requests.BootstrapResponseFactory;
import malkawi.logging.net.global.connections.pollers.AbstractPoller;
import malkawi.logging.net.global.data.Packet;
import malkawi.logging.net.global.requests.AbstractResponse;

@AllArgsConstructor
public class BootstrapPoller extends AbstractPoller {

    private final BootstrapClient client;

    @Override
    protected void pollAction() {
        Packet packet = client.getConnection().receivePacket();
        replyTo(packet);
    }

    public void replyTo(Packet packet) {
        if(packet != null) {
            client.getConnection().setLastReceivedPacket(System.currentTimeMillis());
            AbstractResponse response = BootstrapResponseFactory
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