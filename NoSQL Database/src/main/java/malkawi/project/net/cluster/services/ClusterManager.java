package malkawi.project.net.cluster.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import malkawi.project.net.client.impl.database.data.DatabaseServerInfo;
import malkawi.project.net.cluster.Cluster;
import malkawi.project.net.cluster.requests.BroadcastRequest;
import malkawi.project.net.cluster.requests.ClusterSessionRequest;
import malkawi.project.net.cluster.session.DBClusterSession;
import malkawi.project.net.cluster.session.ClusterSessionStarter;
import malkawi.project.utilities.io.console.Console;

@RequiredArgsConstructor
public class ClusterManager {

    private final @NonNull Cluster cluster;

    public synchronized BroadcastRequest sendBroadcastRequest(String databaseName, Object... requestData) {
        BroadcastRequest broadcastRequest = new BroadcastRequest(databaseName, requestData, cluster);
        cluster.getBroadcaster().addRequest(broadcastRequest);
        return broadcastRequest;
    }

    public ClusterSessionRequest sendRequest(DBClusterSession session, Object... requestData) {
        Console.info("[ClusterManager] submitting request (" + requestData[0] + ")");
        ClusterSessionRequest request = new ClusterSessionRequest(session, requestData, cluster);
        cluster.getServicePool().submit(request);
        return request;
    }

    public void startSession(DatabaseServerInfo serverInfo, String databaseName) {
        cluster.getServicePool().submit(new ClusterSessionStarter(serverInfo, databaseName, cluster));
    }

    public void addSession(DBClusterSession session) {
        if(getClusterSession(session.getServerInfo()) == null) {
            cluster.getNodes().put(session.getServerInfo(), session);
            cluster.getBalancer().addServer(session.getServerInfo());
        }
    }

    public DBClusterSession getClusterSession(String ip, int port) {
        return getClusterSession(new DatabaseServerInfo(ip, port));
    }

    public DBClusterSession getClusterSession(DatabaseServerInfo serverConnectionInfo) {
        return cluster.getNodes().get(serverConnectionInfo);
    }

}
