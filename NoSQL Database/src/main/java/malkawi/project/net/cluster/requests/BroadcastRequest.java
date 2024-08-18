package malkawi.project.net.cluster.requests;

import lombok.Getter;
import lombok.NonNull;
import malkawi.project.net.cluster.Cluster;
import malkawi.project.net.cluster.session.DBClusterSession;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class BroadcastRequest implements Runnable {

    private final String databaseName;

    private final @Getter Object[] requestData;

    private final Cluster cluster;

    private final @Getter List<ClusterSessionRequest> requests;

    private @Getter boolean completedTasks;

    private Queue<DBClusterSession> sessionsToRequest;

    public BroadcastRequest(@NonNull String databaseName, @NonNull Object[] requestData, @NonNull Cluster cluster) {
        this.databaseName = databaseName;
        this.requestData = requestData;
        this.cluster = cluster;
        this.requests = new ArrayList<>();
    }

    @Override
    public void run() {
        populateSessionQueue();
        while (!sessionsToRequest.isEmpty()) {
            DBClusterSession session = sessionsToRequest.poll();
            requests.add(cluster.getManager().sendRequest(session, requestData));
        }
        completedTasks = true;
    }

    private void populateSessionQueue() {
        sessionsToRequest = cluster.getNodes().values().stream().filter(n ->
                n.getAssignedDatabase().equalsIgnoreCase(databaseName)).collect(Collectors.toCollection(LinkedList::new));
    }

}
