package malkawi.project;

import malkawi.project.net.cluster.Cluster;

public class Bootstrap {

    public static void main(String[] args) {
        Cluster.start();
        BootstrapInstance session = new BootstrapInstance();
        session.start();
        addShutdownHook(session);
    }

    private static void addShutdownHook(BootstrapInstance session) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            session.close();
            if(Cluster.isStarted())
                Cluster.get().close();
        }));
    }

}
