package malkawi.project.net.cluster;

import lombok.Getter;
import malkawi.project.data.Config;
import malkawi.project.net.client.impl.database.data.DatabaseServerInfo;
import malkawi.project.net.cluster.data.ClusterNodeData;
import malkawi.project.net.cluster.services.ClusterBalancer;
import malkawi.project.net.cluster.services.ClusterBroadcaster;
import malkawi.project.net.cluster.services.ClusterCleaner;
import malkawi.project.net.cluster.services.ClusterManager;
import malkawi.project.net.cluster.session.DBClusterSession;
import malkawi.project.utilities.Utils;
import malkawi.project.utilities.io.console.Console;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Cluster {

    private final @Getter Map<DatabaseServerInfo, DBClusterSession> nodes;

    private final @Getter ExecutorService servicePool;

    private final @Getter ClusterManager manager;

    private final @Getter ClusterBalancer balancer;

    private final @Getter ClusterNodeData currentNodeData;

    private @Getter ClusterBroadcaster broadcaster;

    private ClusterCleaner cleaner;

    private static Cluster instance = null;

    private Cluster() {
        servicePool = Executors.newCachedThreadPool();
        this.nodes = new ConcurrentHashMap<>();
        this.manager = new ClusterManager(this);
        this.balancer = new ClusterBalancer();
        this.currentNodeData = ClusterNodeData.loadNodeData();
        initServices();
    }

    private void initServices() {
        this.cleaner = new ClusterCleaner(this);
        servicePool.submit(cleaner);
        this.broadcaster = new ClusterBroadcaster(this);
        servicePool.submit(broadcaster);
    }

    public static void start() {
        if(instance == null)
            instance = new Cluster();
    }

    public void initFixedClusterNodes(DatabaseServerInfo[] infos) {
        DatabaseServerInfo currentServerInfo = new DatabaseServerInfo(Utils.getHostName(),
                Config.get().getDatabasePort());
        Console.info("[Cluster] Set balancer to " + currentServerInfo.getIP());
        balancer.setCurrentBalancerHost(currentServerInfo);
        for(DatabaseServerInfo info : infos) {
            if(!info.equals(currentServerInfo))
                nodes.put(info, new DBClusterSession(info, Config.get().getDefaultDatabase()));
            balancer.addServer(info);
        }
    }

    public void close() {
        cleaner.setEnabled(false);
        broadcaster.setEnabled(false);
        nodes.values().forEach(DBClusterSession::close);
        servicePool.shutdown();
    }

    public static Cluster get() {
        return instance;
    }

    public static boolean isStarted() {
        return instance != null;
    }

}
