package malkawi.project.net.cluster.services;

import lombok.Setter;
import malkawi.project.database.components.collections.DocumentCollection;
import malkawi.project.net.client.impl.database.data.DatabaseServerInfo;

import java.util.*;

public class ClusterBalancer {

    private final List<DatabaseServerInfo> orderedServers;

    private @Setter DatabaseServerInfo currentBalancerHost;

    public ClusterBalancer() {
        this.orderedServers = new ArrayList<>();
    }

    public DatabaseServerInfo getAffinityNode(int documentId, DocumentCollection collection) {
        int collectionSize = collection.getLastIndex() + 1;
        int serverIndex = (int) (((double) documentId / collectionSize) * (orderedServers.size()));
        DatabaseServerInfo serverInfo = orderedServers.get(serverIndex);
        if(serverInfo == currentBalancerHost)
            return null;
        return serverInfo;
    }

    public void addServer(DatabaseServerInfo serverInfo) {
        orderedServers.add(serverInfo);
        orderedServers.sort(Comparator.comparingInt(DatabaseServerInfo::getPort));
    }

}
