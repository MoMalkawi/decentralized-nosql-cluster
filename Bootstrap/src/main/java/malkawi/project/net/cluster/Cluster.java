package malkawi.project.net.cluster;

import lombok.Getter;
import malkawi.project.data.Config;
import malkawi.project.net.client.impl.NodeClient;
import malkawi.project.net.cluster.services.ClusterCleaner;
import malkawi.project.net.cluster.services.UserBalancer;
import malkawi.project.net.cluster.services.UserBroadcaster;
import malkawi.project.net.global.data.ServerInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Cluster {

    private final @Getter Map<ServerInfo, NodeClient> nodes;

    private final @Getter ExecutorService servicePool;

    private final @Getter UserBalancer balancer;

    private @Getter UserBroadcaster broadcaster;

    private ClusterCleaner cleaner;

    private static Cluster instance = null;

    private Cluster() {
        servicePool = Executors.newCachedThreadPool();
        this.nodes = new ConcurrentHashMap<>();
        this.balancer = new UserBalancer();
        initServices();
        initServerInfo();
    }

    private void initServices() {
        this.cleaner = new ClusterCleaner(this);
        servicePool.submit(cleaner);
        this.broadcaster = new UserBroadcaster(this);
        servicePool.submit(broadcaster);
    }

    private void initServerInfo() {
        for(ServerInfo info : Config.get().getServerInfos())
            nodes.put(info, new NodeClient(info.getIP(), info.getPort()));
    }

    public void close() {
        cleaner.setEnabled(false);
        broadcaster.setEnabled(false);
        nodes.values().forEach(NodeClient::stop);
        servicePool.shutdown();
    }

    public static void start() {
        if(instance == null)
            instance = new Cluster();
    }

    public static Cluster get() {
        return instance;
    }

    public static boolean isStarted() {
        return instance != null;
    }

}
