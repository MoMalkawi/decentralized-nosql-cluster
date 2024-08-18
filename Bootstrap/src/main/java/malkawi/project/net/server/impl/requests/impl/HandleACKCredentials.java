package malkawi.project.net.server.impl.requests.impl;

import malkawi.project.data.profiles.User;
import malkawi.project.net.cluster.Cluster;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.data.PacketBuilder;
import malkawi.project.net.global.data.ServerInfo;
import malkawi.project.net.global.requests.AbstractResponse;
import malkawi.project.net.server.impl.connection.ClientConnection;
import malkawi.project.utilities.Sleep;
import malkawi.project.utilities.io.console.Console;

public class HandleACKCredentials extends AbstractResponse {

    private static final User ROOT = new User(-1, "root", "root", User.Role.ROOT);

    public HandleACKCredentials(AbstractConnection connection, Packet requestPacket) {
        super(connection, requestPacket);
    }

    @Override
    public void respond() {
        Console.info("[HandleCredentials] Processing Request...");
        try {
            Object rawUser = requestPacket.getContent()[0];
            if (!(rawUser instanceof User)) {
                Console.info("[ACK] Received No User Details.");
                sendCredentials();
                return;
            }
            User user = (User) rawUser;
            if(user.getRole() == null)
                user.setRole(User.Role.USER);
            if (user.equals(ROOT) && user.getRole().equals(User.Role.ROOT)) { //server
                Console.info("[ACK] Received Node Authorization.");
                ((ClientConnection) connection).setLoggedUser(ROOT);
                return;
            }
            handleExistingCredentials(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendCredentials() {
        User newUser = ((ClientConnection) connection).getServer().getInstance().getUserManager().generateUser();
        Cluster.get().getBroadcaster().addUser(newUser);
        Console.info("[ACK] Account sent to broadcaster, sleeping for up to 5s to allow for process completion.");
        Sleep.sleep(100);
        Sleep.sleepUntil(() -> Cluster.get().getBroadcaster().isBroadcasting(), 5000, 50);
        ServerInfo serverInfo = Cluster.get().getBalancer().getMostFreeNode();
        Console.info("[ACK] Sending new (" + newUser.getUsername() + ") credentials and connection details.");
        connection.send(new PacketBuilder().typeName("NEW_CREDENTIALS")
                .values(newUser, serverInfo.getIP(), serverInfo.getPort()).build());
        Console.success("[ACK] Sent credentials and connection information. (user, ip, port)");
        Console.info("[ACK] Finished duties, aborting [USER] connection.");
        connection.terminate();
    }

    private void handleExistingCredentials(User user) {
        Console.info("[ACK] Received existing user node request...");
        if(((ClientConnection) connection).getServer().getInstance()
                .getUserManager().getUsers().contains(user) || user.equals(ROOT)) {
            Console.info("[ACK] Found user.");
            ServerInfo serverInfo = Cluster.get().getBalancer().getMostFreeNode();
            Console.info("[ACK] Sending existing (" + user.getUsername() + ") account its connection credentials.");
            connection.send(new PacketBuilder().typeName("NEW_CREDENTIALS")
                    .values(user, serverInfo.getIP(), serverInfo.getPort()).build());
            Console.success("[ACK] Sent credentials and connection information. (user, ip, port)");
            Console.info("[ACK] Finished duties, aborting [USER] connection.");
            connection.terminate();
            return;
        }
        Console.info("[ACK] Account wasn't found, aborting...");
        connection.send(new PacketBuilder().typeName("TERMINATE").values("Failed authentication").build());
        connection.terminate();
    }

    @Override
    public boolean validate() {
        return checkContentLength(1, true);
    }

}
