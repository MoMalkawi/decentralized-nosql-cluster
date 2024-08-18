package malkawi.project;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import malkawi.project.services.BootstrapService;
import malkawi.project.net.cluster.Cluster;
import malkawi.project.net.server.impl.DatabasesServer;
import malkawi.project.utilities.Sleep;

@Getter
@RequiredArgsConstructor
public class DatabaseNode {

    private final @NonNull int bootstrapPort;

    private @Setter DatabasesServer server;

    public boolean start() {
        BootstrapService bootstrapService = new BootstrapService(this);
        bootstrapService.populateFromBootstrapper();
        if(Sleep.sleepUntil(bootstrapService::isPopulated, 60000, 50)) {
            server.start();
            return true;
        }
        return false;
    }

    public void shutDown() {
        if(server != null)
            server.close();
        if(Cluster.isStarted())
            Cluster.get().close();
    }

}
