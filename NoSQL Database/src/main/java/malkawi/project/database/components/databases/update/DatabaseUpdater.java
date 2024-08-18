package malkawi.project.database.components.databases.update;

import malkawi.project.database.components.databases.Database;
import malkawi.project.database.io.update.DatabaseFilesUpdater;
import malkawi.project.net.cluster.Cluster;
import malkawi.project.net.cluster.update.ClusterDBUpdater;

import java.util.*;

public class DatabaseUpdater implements Observer {

    private final DatabaseFilesUpdater filesUpdater;

    private final ClusterDBUpdater clusterUpdater;

    public DatabaseUpdater(Database database) {
        this.filesUpdater = new DatabaseFilesUpdater(database);
        this.clusterUpdater = new ClusterDBUpdater(database);
    }

    @Override
    public void update(Observable o, Object arg) {
        boolean updateCluster = (boolean) arg;
        if(filesUpdater.createDatabaseBase()) {
            removeUselessCollections(updateCluster);
            createCollections(updateCluster);
            filesUpdater.updateDatabaseMetaData();
        }
        Cluster.get().getCurrentNodeData().updateTime().updateDataFile();
    }

    private void createCollections(boolean updateCluster) {
        List<Integer> collectionIndices = filesUpdater.getNotRegisteredCollections();
        if(collectionIndices != null && !collectionIndices.isEmpty()) {
            filesUpdater.createCollections(collectionIndices);
            if(updateCluster)
                clusterUpdater.insertCollections(collectionIndices);
        }
    }

    private void removeUselessCollections(boolean updateCluster) {
        List<Integer> collectionIndices = filesUpdater.getZombieCollections();
        if(collectionIndices != null && !collectionIndices.isEmpty()) {
            filesUpdater.removeCollections(collectionIndices);
            if(updateCluster)
                clusterUpdater.removeCollections(collectionIndices);
        }
    }

}
