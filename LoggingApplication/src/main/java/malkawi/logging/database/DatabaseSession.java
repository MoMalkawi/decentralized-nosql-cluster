package malkawi.logging.database;

import lombok.Getter;
import malkawi.logging.database.data.users.User;
import malkawi.logging.net.client.impl.database.DatabaseClient;
import malkawi.logging.net.client.impl.database.data.ServerInfo;
import malkawi.logging.net.global.data.Packet;
import malkawi.logging.net.global.data.PacketBuilder;
import malkawi.logging.utilities.Sleep;
import malkawi.logging.utilities.io.console.Console;

//BootstrapHandler generates serverInfo and user
//Pool asks BootstrapHandler to generate and provides the database connection by returning .connect()
public class DatabaseSession implements AutoCloseable {

    private final @Getter DatabaseClient client;

    private String databaseName;

    public DatabaseSession(ServerInfo serverInfo, User user, String databaseName) {
        this.client = new DatabaseClient(serverInfo, user);
        this.databaseName = databaseName;
    }

    public DatabaseSession connect() {
        if(!client.isAlive()) {
            Console.info("[Client] Starting session...");
            client.start();
            if(Sleep.sleepUntil(client::isAlive, 1000, 10)) {
                synchronizeSession();
                loginDatabase();
            }
        }
        return this;
    }

    private void synchronizeSession() {
        if(!client.isAlive())
            return;
        Console.info("[Synchronization] started.");
        client.getConnection().send(new PacketBuilder().typeName("SYN").build());
        Sleep.sleepUntil(this::isConnected, 10000, 50);
    }

    public void loginDatabase(String databaseName) {
        this.databaseName = databaseName;
        loginDatabase();
    }

    private void loginDatabase() {
        if(!isConnected() || databaseName == null)
            return;
        Console.info("[Database] Attempting to log into database.");
        Packet loginPacket = new PacketBuilder().typeName("USE_DATABASE")
                .values(databaseName).generateIdentifier().build();
        client.getConnection().send(loginPacket);
        Sleep.sleepUntil(this::isDatabaseLogged, 10000, 50);
    }

    public boolean isDatabaseLogged() {
        if(databaseName == null || client.getDatabaseConnection().getLoggedDatabase() == null)
            return false;
        return databaseName.equalsIgnoreCase(client.getDatabaseConnection().getLoggedDatabase());
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
