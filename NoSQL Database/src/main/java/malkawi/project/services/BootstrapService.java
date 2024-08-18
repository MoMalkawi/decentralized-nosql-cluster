package malkawi.project.services;

import lombok.Getter;
import malkawi.project.DatabaseNode;
import malkawi.project.net.client.impl.bootstrap.BootstrapClient;
import malkawi.project.net.client.impl.bootstrap.connection.BootstrapConnection;
import malkawi.project.net.global.data.PacketBuilder;

@Getter
public class BootstrapService {

    private final BootstrapClient client;

    public BootstrapService(DatabaseNode node) {
        this.client = new BootstrapClient(node);
    }

    public void populateFromBootstrapper() {
        startBootstrapClient();
        client.getConnection().send(new PacketBuilder().typeName("REQUEST_NODE_SETUP").build());
    }

    private void startBootstrapClient() {
        if(!client.isAlive())
            client.start();
    }

    public boolean isPopulated() {
        return ((BootstrapConnection) client.getConnection()).isSuccessfulPopulation();
    }

}
