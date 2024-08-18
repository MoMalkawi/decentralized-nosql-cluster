package malkawi.project.net.server.impl.poller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import malkawi.project.net.global.connections.pollers.AbstractPoller;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.requests.AbstractResponse;
import malkawi.project.net.server.impl.connection.ClientConnection;
import malkawi.project.net.server.impl.requests.ServerResponseFactory;
import malkawi.project.utilities.io.console.Console;

@RequiredArgsConstructor
public class ClientPoller extends AbstractPoller {

    private final @NonNull ClientConnection connection;

    @Override
    protected void pollAction() {
        if(!connection.isAlive()) {
            setEnabled(false);
            return;
        }
        Packet packet = connection.receivePacket();
        replyTo(packet);
    }

    public void replyTo(Packet packet) {
        if(packet != null) {
            Console.info("[BootstrapClientPoller] received (" + packet.getTypeName() + ") packet.");
            connection.setLatestActivityTimeMillis(System.currentTimeMillis());
            AbstractResponse response = ServerResponseFactory
                    .getResponse(connection, packet);
            if(response != null && response.validate())
                response.respond();
        }
    }

    @Override
    protected boolean validatePolling() {
        return connection.isAlive();
    }

}
