package malkawi.project.net.cluster.session;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import malkawi.project.net.client.impl.database.data.DatabaseServerInfo;
import malkawi.project.net.cluster.Cluster;

@RequiredArgsConstructor
public class ClusterSessionStarter implements Runnable {

    private final @NonNull DatabaseServerInfo serverInfo;

    private final @NonNull String assignedDatabase;

    private final Cluster cluster;

    private @Getter boolean success;

    private @Getter boolean completedTasks;

    @Override
    public void run() {
        DBClusterSession session = getExistingSession();
        if(session != null)
            success = handleExistingSession(session);
        else {
            session = new DBClusterSession(serverInfo, assignedDatabase);
            if (session.connect().isConnected()) {
                cluster.getNodes().put(serverInfo, session);
                success = true;
            }
        }
        completedTasks = true;
    }

    private boolean handleExistingSession(DBClusterSession session) {
        if(session.isConnected())
            return true;
        return cluster.getManager().getClusterSession(serverInfo).connect().isConnected();
    }

    private DBClusterSession getExistingSession() {
        return cluster.getManager().getClusterSession(serverInfo);
    }

}
