package malkawi.project.net.server.impl.requests.impl;

import malkawi.project.data.Config;
import malkawi.project.data.profiles.User;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.data.PacketBuilder;
import malkawi.project.net.global.requests.AbstractResponse;
import malkawi.project.net.server.impl.connection.ClientConnection;
import malkawi.project.utilities.io.console.Console;

public class SendNodeSetup extends AbstractResponse {

    public SendNodeSetup(AbstractConnection connection, Packet requestPacket) {
        super(connection, requestPacket);
    }

    @Override
    public void respond() {
        Console.info("[SendNodeSetup] Sending setup date to (" +
                connection.getSocket().getInetAddress().getHostName() +
                ":" + connection.getSocket().getPort() + ")");
        try {
            connection.send(new PacketBuilder().typeName("SYSTEM_SETUP")
                    .values(
                            Config.get().getDatabases(),
                            getUsers(),
                            Config.get().getRawServerInfos()
                    ).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Console.info("[SendNodeSetup] Sent setup data.");
    }

    private User[] getUsers() {
        return ((ClientConnection) connection).getServer().getInstance().getUserManager().getUsers().toArray(new User[0]);
    }

    @Override
    public boolean validate() {
        return checkContentLength(0, false);
    }

}
