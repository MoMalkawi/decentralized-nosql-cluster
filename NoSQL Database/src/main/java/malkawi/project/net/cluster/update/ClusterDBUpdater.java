package malkawi.project.net.cluster.update;

import lombok.AllArgsConstructor;
import malkawi.project.database.components.collections.DocumentCollection;
import malkawi.project.database.components.databases.Database;
import malkawi.project.net.cluster.Cluster;
import malkawi.project.utilities.io.console.Console;

import java.util.List;

@AllArgsConstructor
public class ClusterDBUpdater {

    private final Database database;

    public void insertCollections(List<Integer> collectionIndices) {
        Console.info("[Cluster Database] Submitting collection insertion request.");
        collectionIndices.forEach(i -> {
            DocumentCollection collection = database.getManager().getCollection(i);
            if(collection != null)
                Cluster.get().getManager().sendBroadcastRequest(database.getName(),
                        "insert-collection", collection.getId(), collection.getName(),
                        collection.getSchema().getRawSchemaProperties());
        });
    }

    public void removeCollections(List<Integer> collectionIndices) {
        Console.info("[Cluster Database] Submitting collection removal request.");
        collectionIndices.forEach(i -> Cluster.get().getManager().sendBroadcastRequest(database.getName(),
                    "remove-collection-nodal", i));
    }

}
