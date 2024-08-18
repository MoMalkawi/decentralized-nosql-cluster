package malkawi.project.net.cluster.services;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import malkawi.project.data.profiles.User;
import malkawi.project.net.client.impl.NodeClient;
import malkawi.project.net.cluster.Cluster;
import malkawi.project.net.global.data.PacketBuilder;
import malkawi.project.utilities.Sleep;
import malkawi.project.utilities.io.console.Console;

import java.util.LinkedList;
import java.util.Queue;

@RequiredArgsConstructor
public class UserBroadcaster implements Runnable {

    private final Queue<User> usersQueue = new LinkedList<>();

    private @Setter boolean enabled = true;

    private final @NonNull Cluster cluster;

    private @Getter boolean broadcasting = false;

    @Override
    public void run() {
        while(enabled) {
            if(!usersQueue.isEmpty())
                processQueue();
            else if(broadcasting)
                broadcasting = false;
            Sleep.sleep(1000);
        }
    }

    private void processQueue() {
        broadcasting = true;
        User user = usersQueue.poll();
        if(user == null)
            return;
        Console.info("[Broadcaster] Broadcasting (" + user.getUsername() + ") Account");
        broadCast(user);
    }

    private void broadCast(User user) {
        cluster.getNodes().values().forEach(n -> {
            if(checkClientValidity(n)) {
                n.getConnection().send(new PacketBuilder().typeName("ADD_USER").values(user).build());
                Console.info("[Broadcaster] Sent (ADD_USER) request to node (" + n.getIp() + ":" + n.getPort() + ")");
            }
        });
    }

    private boolean checkClientValidity(NodeClient client) {
        if(client.isAlive())
            return true;
        Console.info("[Broadcaster] Found dead connection, attempting connection ignition...");
        client.start();
        if(!Sleep.sleepUntil(client::isAlive, 1000, 10))
            Console.info("[Broadcaster] Failed to ignite a connection.");
        return client.isAlive();
    }

    public void addUser(User user) {
        usersQueue.add(user);
    }

}
