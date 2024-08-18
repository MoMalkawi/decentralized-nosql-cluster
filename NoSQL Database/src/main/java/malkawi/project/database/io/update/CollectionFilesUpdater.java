package malkawi.project.database.io.update;

import lombok.AllArgsConstructor;
import malkawi.project.data.Config;
import malkawi.project.database.components.collections.DocumentCollection;
import malkawi.project.database.components.collections.documents.Document;
import malkawi.project.utilities.Utils;
import malkawi.project.utilities.io.IOUtils;
import malkawi.project.utilities.io.JSONUtilities;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CollectionFilesUpdater {

    private final DocumentCollection collection;

    public void appendDocuments(List<Document> documents) {
        documents.forEach(d -> {
            collection.getDocuments().get(d.getId()).setChanged(false);
            JSONUtilities.overwriteJSONFile(d, getDocumentPath(d.getId()));
        });
    }

    public List<Document> getChangedDocuments() {
        return collection.getDocuments().values().stream().
                filter(Document::isChanged).collect(Collectors.toList());
    }

    public void removeUselessDocuments(List<Integer> documentsIndices) {
        documentsIndices.forEach(d -> IOUtils.deleteFile(getDocumentPath(d)));
    }

    public List<Integer> getZombieDocuments() {
        List<String> documentsNames = IOUtils.getFileNames(getCollectionPath(),
                name -> Utils.NUMBERS_ONLY.matcher(name).find());
        return documentsNames.stream().filter(n ->
                !collection.getDocuments().containsKey(Integer.parseInt(n)))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public void updateSchema() {
        if(collection.getSchema().isChanged()) {
            File schemaFile = getCollectionFile("schema.collection");
            JSONUtilities.overwriteJSONFile(collection.getSchema(), schemaFile);
            collection.getSchema().setChanged(false);
        }
    }

    public void updateCollectionMetaData() {
        File metaFile = getCollectionFile("meta.collection");
        JSONUtilities.overwriteJSONFile(collection, metaFile);
    }

    public boolean canAccessCollection() {
        return getCollectionPath().exists();
    }

    private File getCollectionPath() {
        return new File(Utils.buildPath(Config.get().getDatabasesRootPath(),
                String.valueOf(collection.getDatabaseId()), String.valueOf(collection.getId())));
    }

    private File getDocumentPath(int documentId) {
        return new File(Utils.buildPath(
                Config.get().getDatabasesRootPath(), String.valueOf(collection.getDatabaseId()),
                String.valueOf(collection.getId()), String.valueOf(documentId)));
    }

    private File getCollectionFile(String fileName) {
        return new File(Utils.buildPath(Config.get().getDatabasesRootPath(),
                String.valueOf(collection.getDatabaseId()), String.valueOf(collection.getId()), fileName));
    }

}
