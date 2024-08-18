package malkawi.project.net.client.impl.bootstrap.requests.impl;

import malkawi.project.data.Config;
import malkawi.project.database.components.databases.Database;
import malkawi.project.database.components.users.User;
import malkawi.project.net.client.impl.bootstrap.connection.BootstrapConnection;
import malkawi.project.net.client.impl.database.data.DatabaseServerInfo;
import malkawi.project.net.cluster.Cluster;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.requests.AbstractResponse;
import malkawi.project.net.server.impl.DatabasesServer;
import malkawi.project.utilities.io.console.Console;

import java.util.Arrays;

public class ReceiveSystemSetup extends AbstractResponse {

    private final BootstrapConnection bootstrapConnection;

    public ReceiveSystemSetup(AbstractConnection connection, Packet requestPacket) {
        super(connection, requestPacket);
        this.bootstrapConnection = (BootstrapConnection) connection;
    }

    @Override
    public void respond() {
        Console.info("[ReceiveSystemSetup] Receiving...");
        if(createServer()) {
            try {
                addUsers();
                addDatabases();
                initClusterNodes();
                bootstrapConnection.setSuccessfulPopulation(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initClusterNodes() {
        DatabaseServerInfo[] serverInfos = getDatabaseServerInfos();
        Cluster.get().initFixedClusterNodes(serverInfos);
        Console.info("[ReceiveSystemSetup] Initialized Cluster Nodes and Balancer.");
    }

    private void addDatabases() {
        String[] databases = getDatabaseNames();
        if(databases != null) {
            int index = 0;
            for(String databaseName : databases) {
                bootstrapConnection.getClient().getNode()
                        .getServer().getDatabaseSystem().getManager()
                        .insertDatabaseIfNotExist(new Database(index, databaseName));
                index++;
            }
            Console.info("[ReceiveSystemSetup] Processed (" + index + ") databases.");
        }
    }

    private void addUsers() {
        User[] users = getUsers();
        if(users != null) {
            bootstrapConnection.getClient().getNode().getServer().getDatabaseSystem().getUserManager().addUsers(users);
            Console.info("[ReceiveSystemSetup] Processed Users.");
        }
    }

    private boolean createServer() {
        bootstrapConnection.getClient().getNode().setServer(new DatabasesServer(Config.get().getDatabasePort()));
        Console.info("[ReceiveSystemSetup] Created server from  port: " + Config.get().getDatabasePort());
        return true;
    }

    private DatabaseServerInfo[] getDatabaseServerInfos() {
        String[] rawInfo = (String[]) requestPacket.getContent()[2];
        return Arrays.stream(rawInfo).map(i -> {
            String[] splitted = i.split(":");
            return new DatabaseServerInfo(splitted[0], Integer.parseInt(splitted[1]));
        }).toArray(DatabaseServerInfo[]::new);
    }

    private User[] getUsers() {
        Object rawUsers = requestPacket.getContent()[1];
        return rawUsers instanceof User[] ? (User[]) requestPacket.getContent()[1] : null;
    }

    private String[] getDatabaseNames() {
        Object rawDatabases = requestPacket.getContent()[0];
        return rawDatabases instanceof String[] ? (String[]) rawDatabases : null;
    }


    @Override
    public boolean validate() {
        return checkContentLength(3, true);
    }

}
