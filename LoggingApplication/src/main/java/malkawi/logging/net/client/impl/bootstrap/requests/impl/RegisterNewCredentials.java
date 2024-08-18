package malkawi.logging.net.client.impl.bootstrap.requests.impl;

import malkawi.logging.database.data.users.User;
import malkawi.logging.net.client.impl.bootstrap.connection.BootstrapConnection;
import malkawi.logging.net.client.impl.database.data.ServerInfo;
import malkawi.logging.net.global.connections.AbstractConnection;
import malkawi.logging.net.global.data.Packet;
import malkawi.logging.net.global.requests.AbstractResponse;
import malkawi.logging.utilities.io.console.Console;

public class RegisterNewCredentials extends AbstractResponse {

    private final BootstrapConnection bootstrapConnection;

    public RegisterNewCredentials(AbstractConnection connection, Packet requestPacket) {
        super(connection, requestPacket);
        this.bootstrapConnection = (BootstrapConnection) connection;
    }

    @Override
    public void respond() {
        Console.info("[Bootstrap] Received credentials, registering them...");
        User user = (User) requestPacket.getContent()[0];
        String ip = (String) requestPacket.getContent()[1];
        int port = ((Number) requestPacket.getContent()[2]).intValue();
        bootstrapConnection.getClient().getService().setUser(user);
        bootstrapConnection.getClient().getService().setDesignatedServerInfo(new ServerInfo(ip, port));
        Console.success("[Bootstrap] Processed user: (" + user.getUsername() + "), network: ("
                + ip + ":" + port + ")");
    }

    @Override
    public boolean validate() {
        return checkContentLength(3, true) && requestPacket.getContent()[0] instanceof User;
    }

}
