package malkawi.project.net.cluster.session;

import lombok.Getter;
import malkawi.project.net.client.impl.database.DatabaseClient;
import malkawi.project.net.client.impl.database.data.DatabaseServerInfo;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.data.PacketBuilder;
import malkawi.project.utilities.Sleep;
import malkawi.project.utilities.io.console.Console;

@Getter
public class DBClusterSession implements AutoCloseable {

    private final DatabaseServerInfo serverInfo;

    private final String assignedDatabase;

    private final DatabaseClient client;

    public DBClusterSession(DatabaseServerInfo serverInfo, String assignedDatabase) {
        this.serverInfo = serverInfo;
        this.assignedDatabase = assignedDatabase;
        this.client = new DatabaseClient(serverInfo);
    }

    public DBClusterSession connect() {
        if(!client.isAlive()) {
            Console.info("[Client] Starting session for " + serverInfo);
            client.start();
            if(Sleep.sleepUntil(client::isAlive, 1000, 10)) {
                synchronizeSession(3);
                loginDatabase(3);
            }
        }
        return this;
    }

    private void synchronizeSession(int attempts) {
        if(!client.isAlive())
            return;
        Console.info("[Synchronization] started.");
        client.getConnection().send(new PacketBuilder().typeName("SYN").build());
        Sleep.sleepUntil(this::isConnected, 10000, 50);
        if(!isConnected() && attempts > 0) {
            Console.error("[Synchronization] Attempt (" + (4 - attempts) + ") failed. trying again...");
            attempts--;
            Sleep.sleep(10);
            synchronizeSession(attempts);
        }
    }

    private void loginDatabase(int attempts) {
        if(!isConnected())
            return;
        Console.info("[Database] Attempting to log into database. (" + (4 - attempts) + "/3).");
        Packet loginPacket = new PacketBuilder().typeName("USE_DATABASE")
                .values(assignedDatabase).generateIdentifier().build();
        client.getConnection().send(loginPacket);
        Sleep.sleepUntil(this::isDatabaseLogged, 10000, 50);
        if(!isDatabaseLogged() && attempts > 0) {
            Console.error("[Database] Couldn't log into (" + assignedDatabase + ").");
            attempts--;
            Sleep.sleep(10);
            loginDatabase(attempts);
        }
    }

    public boolean isDatabaseLogged() {
        if(client.getDatabaseConnection().getLoggedDatabase() == null)
            return false;
        return assignedDatabase.equalsIgnoreCase(client.getDatabaseConnection().getLoggedDatabase());
    }

    @Override
    public void close() {
        if(client.isAlive())
            client.stop();
    }

    public boolean isConnected() {
        return client.isAlive() && client.getDatabaseConnection().isUserAuthenticated();
    }

}
