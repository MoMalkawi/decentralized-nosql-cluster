package malkawi.project.net.server.impl.pollers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import malkawi.project.net.global.connections.pollers.AbstractPoller;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.requests.AbstractResponse;
import malkawi.project.net.server.impl.connection.DBClientConnection;
import malkawi.project.net.server.impl.requests.DBServerResponseFactory;
import malkawi.project.utilities.io.console.Console;

@RequiredArgsConstructor
public class DatabaseClientPoller extends AbstractPoller {

    private final @NonNull DBClientConnection connection;

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
            Console.info("[ClientPoller] received (" + packet.getTypeName() + ") packet.");
            connection.setLatestActivityTimeMillis(System.currentTimeMillis());
            AbstractResponse response = DBServerResponseFactory
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
