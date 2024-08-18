package malkawi.project;

import malkawi.project.net.cluster.Cluster;
import malkawi.project.utilities.io.console.Console;

public class Main {

    public static void main(String[] args) {
        Cluster.start();
        DatabaseNode node = new DatabaseNode(14000);
        addShutdownHook(node);
        if(node.start())
            Console.success("[Main] Database Node started successfully. Ready for requests!");
    }

    private static void addShutdownHook(DatabaseNode node) {
        Runtime.getRuntime().addShutdownHook(new Thread(node::shutDown));
    }

}
