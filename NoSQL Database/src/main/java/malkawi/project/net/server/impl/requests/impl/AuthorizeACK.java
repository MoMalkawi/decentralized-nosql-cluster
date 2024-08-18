package malkawi.project.net.server.impl.requests.impl;

import malkawi.project.database.components.users.User;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.data.PacketBuilder;
import malkawi.project.net.global.requests.AbstractResponse;
import malkawi.project.net.server.impl.connection.DBClientConnection;
import malkawi.project.utilities.io.console.Console;

public class AuthorizeACK extends AbstractResponse {

    public AuthorizeACK(AbstractConnection connection, Packet requestPacket) {
        super(connection, requestPacket);
    }

    @Override
    public void respond() {
        // when a user wants to update a document and the server doesn't have the required affinity
        // have the server log onto the other server as root and perform the update.
        User user = (User) requestPacket.getContent()[0];
        Console.info("[Auth] Authorizing (" + user.getUsername() + ")");
        boolean verified = ((DBClientConnection) connection).getSession()
                .getSystem().getUserManager().verifyUser(user);
        if(verified) {
            ((DBClientConnection) connection).getSession().setUser(user);
            Console.success("[Auth] Successfully authorized (" + user.getUsername() + ")");
            connection.send(new PacketBuilder().typeName("AUTH_SUCCESS").values(user).build());
        } else {
            Console.error("[Auth] Authorization failed for (" + user.getUsername() + ")");
            connection.send(new PacketBuilder().typeName("TERMINATE").values("Failed authentication").build());
            connection.terminate();
            Console.error("[Auth] A client connection has been terminated due to an authentication fail.");
        }
    }

    @Override
    public boolean validate() {
        return checkContentLength(1, true) && requestPacket.getContent()[0] instanceof User;
    }

}
