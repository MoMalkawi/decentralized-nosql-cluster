package malkawi.project.net.cluster.requests;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import malkawi.project.database.data.Result;
import malkawi.project.net.cluster.Cluster;
import malkawi.project.net.cluster.session.DBClusterSession;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.data.PacketBuilder;
import malkawi.project.utilities.Sleep;
import malkawi.project.utilities.io.console.Console;

@RequiredArgsConstructor
public class ClusterSessionRequest implements Runnable {

    private @NonNull DBClusterSession session;

    private final @NonNull Object[] requestData;

    private final @NonNull Cluster cluster;

    private @Getter boolean completedTasks;

    private @Getter Result result;

    @Override
    public void run() {
        try {
            if(startSession())
                performRequest();
            completedTasks = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean startSession() {
        if(session.isConnected())
            return true;
        session = cluster.getManager().getClusterSession(session.getServerInfo()).connect();
        return session.isConnected();
    }

    private void performRequest() {
        Packet requestPacket = new PacketBuilder().typeName("API")
                .values(requestData).generateIdentifier().build();
        Console.info("[ClusterSessionRequest] sent (" + requestPacket.getContent()[0] + ") request. Identifier: " + requestPacket.getIdentifier());
        session.getClient().getConnection().send(requestPacket);
        if (Sleep.sleepUntil(() -> session.getClient().getDatabaseConnection()
                .getResultsCache().containsKey(requestPacket.getIdentifier()), 20000, 50))
            fetchResult(requestPacket.getIdentifier());
        Console.info("[ClusterSessionRequest] finished tasks.");
    }

    private void fetchResult(long identifier) {
        Console.success("[ClusterSessionRequest] received result with identifier: " + identifier);
        result = session.getClient().getDatabaseConnection()
                .getResultsCache().get(identifier);
        removeResult(identifier);
    }

    private void removeResult(long identifier) {
        session.getClient().getDatabaseConnection().getResultsCache().remove(identifier);
    }

}
