package malkawi.project.database.components.collections.update;

import malkawi.project.database.components.collections.DocumentCollection;
import malkawi.project.database.components.collections.documents.Document;
import malkawi.project.database.io.update.CollectionFilesUpdater;
import malkawi.project.net.cluster.Cluster;
import malkawi.project.net.cluster.update.ClusterCollectionUpdater;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CollectionUpdater implements Observer {

    private final CollectionFilesUpdater filesUpdater;

    private final ClusterCollectionUpdater clusterUpdater;

    public CollectionUpdater(DocumentCollection collection) {
        this.filesUpdater = new CollectionFilesUpdater(collection);
        this.clusterUpdater = new ClusterCollectionUpdater(collection);
    }

    @Override
    public void update(Observable o, Object arg) {
        boolean updateCluster = (boolean) arg;
        if(filesUpdater.canAccessCollection()) {
            removeUselessDocuments(updateCluster);
            addNewDocuments(updateCluster);
            filesUpdater.updateSchema();
            filesUpdater.updateCollectionMetaData();
        }
        Cluster.get().getCurrentNodeData().updateTime().updateDataFile();
    }

    private void removeUselessDocuments(boolean updateCluster) {
        List<Integer> documentsIndices = filesUpdater.getZombieDocuments();
        if(documentsIndices != null && !documentsIndices.isEmpty()) {
            filesUpdater.removeUselessDocuments(documentsIndices);
            if(updateCluster)
                clusterUpdater.removeDocuments(documentsIndices);
        }
    }

    private void addNewDocuments(boolean updateCluster) {
        List<Document> changedDocuments = filesUpdater.getChangedDocuments();
        if(changedDocuments != null && !changedDocuments.isEmpty()) {
            filesUpdater.appendDocuments(changedDocuments);
            if(updateCluster)
                clusterUpdater.appendDocuments(changedDocuments);
        }
    }

}
