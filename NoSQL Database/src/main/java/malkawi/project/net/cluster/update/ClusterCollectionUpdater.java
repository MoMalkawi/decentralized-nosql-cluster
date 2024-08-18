package malkawi.project.net.cluster.update;

import lombok.AllArgsConstructor;
import malkawi.project.database.components.collections.DocumentCollection;
import malkawi.project.database.components.collections.documents.Document;
import malkawi.project.net.cluster.Cluster;
import malkawi.project.utilities.io.console.Console;

import java.util.List;

//TODO: (reminder) change "logs" to the database declaration.
@AllArgsConstructor
public class ClusterCollectionUpdater {

    private final DocumentCollection collection;

    public void appendDocuments(List<Document> documents) {
        Console.info("[Cluster Collections] Submitting document append request.");
        documents.forEach(d -> Cluster.get().getManager().sendBroadcastRequest("logs",
                "insert-document", collection.getId(), d.getId(), collection.getManager().getRawPropertyValues(d)));
    }

    public void removeDocuments(List<Integer> documentsIndices) {
        Console.info("[Cluster Collections] Submitting document clean-up/removal request.");
        documentsIndices.forEach(i -> Cluster.get().getManager().sendBroadcastRequest("logs",
                "remove-document-nodal", collection.getId(), i));
    }

}
