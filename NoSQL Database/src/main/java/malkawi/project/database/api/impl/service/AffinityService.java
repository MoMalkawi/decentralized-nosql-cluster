package malkawi.project.database.api.impl.service;

import lombok.AllArgsConstructor;
import malkawi.project.database.DBSystemSession;
import malkawi.project.database.components.collections.DocumentCollection;
import malkawi.project.database.data.Result;
import malkawi.project.net.client.impl.database.data.DatabaseServerInfo;
import malkawi.project.net.cluster.Cluster;
import malkawi.project.net.cluster.requests.ClusterSessionRequest;
import malkawi.project.utilities.Sleep;
import malkawi.project.utilities.io.console.Console;

@AllArgsConstructor
public class AffinityService {

    private final DBSystemSession session;

    public Result redirectUpdateRequest(int collectionId, int documentId, Object[] oldPropertyValues, Object[] propertyValues,
                                        DatabaseServerInfo serverInfo) {
        Console.info("[AffinityService] Redirecting update request to (" + serverInfo.getIP() + ":" + serverInfo.getPort() + ")");
        ClusterSessionRequest request = Cluster.get().getManager().sendRequest(Cluster.get().getManager()
                        .getClusterSession(serverInfo), "update-document-col-id", documentId, collectionId,
                oldPropertyValues, propertyValues);
        Sleep.sleepUntil(request::isCompletedTasks, 30000, 10);
        Result result = request.getResult();
        return (result != null && result.isSuccess()) ? result : Result.FAILURE;
    }

    public DatabaseServerInfo getUpdateAffinityOwner(DocumentCollection collection, int documentId) {
        if(session.getDatabase() == null)
            return null;
        return Cluster.get().getBalancer().getAffinityNode(documentId, collection);
    }

}
