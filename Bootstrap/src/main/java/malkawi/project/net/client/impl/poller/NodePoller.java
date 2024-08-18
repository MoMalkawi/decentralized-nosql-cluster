package malkawi.project.net.client.impl.poller;

import lombok.AllArgsConstructor;
import malkawi.project.net.client.impl.NodeClient;
import malkawi.project.net.client.impl.requests.NodeResponseFactory;
import malkawi.project.net.global.connections.pollers.AbstractPoller;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.requests.AbstractResponse;

@AllArgsConstructor
public class NodePoller extends AbstractPoller {

    private final NodeClient client;

    @Override
    protected void pollAction() {
        Packet packet = client.getConnection().receivePacket();
        replyTo(packet);
    }

    public void replyTo(Packet packet) {
        if(packet != null) {
            client.getConnection().setLatestActivityTimeMillis(System.currentTimeMillis());
            AbstractResponse response = NodeResponseFactory
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
