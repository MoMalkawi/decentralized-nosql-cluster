package malkawi.project.database.io.loaders;

import lombok.AllArgsConstructor;
import malkawi.project.database.components.databases.Database;
import malkawi.project.database.components.collections.DocumentCollection;
import malkawi.project.database.components.collections.data.CollectionSchema;
import malkawi.project.database.components.collections.documents.Document;
import malkawi.project.utilities.Utils;
import malkawi.project.utilities.io.IOUtils;
import malkawi.project.utilities.io.JSONUtilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DatabasesLoader {

    private final String databasesRootPath;

    public List<Database> readDatabases() {
        List<Database> results = new ArrayList<>();
        getIndexFiles(databasesRootPath).forEach(i -> {
            Database db = readDatabase(i);
            if(db != null) {
                results.add(db);
                db.setLive();
            }
        });
        return results;
    }

    private Database readDatabase(int index) {
        Database database = JSONUtilities.readJSONFile(
                getDatabasePath(index) + File.separator + "meta.db", Database.class);
        if(database != null) {
            List<DocumentCollection> collections = readCollections(index);
            database.getManager().insertCollections(collections);
        }
        return database;
    }

    private List<DocumentCollection> readCollections(int index) {
        List<DocumentCollection> results = new ArrayList<>();
        getIndexFiles(getDatabasePath(index)).forEach(i -> {
            String collectionPath = getCollectionPath(i, index);
            DocumentCollection collection = JSONUtilities.readJSONFile(
                    collectionPath + File.separator + "meta.collection",
                    DocumentCollection.class);
            if(collection != null) {
                collection.setSchema(readSchema(collectionPath));
                collection.getManager().insertDocuments(readDocuments(collectionPath));
                results.add(collection);
                collection.setLive();
            }
        });
        return results;
    }

    private List<Document> readDocuments(String collectionPath) {
        List<Document> documents = new ArrayList<>();
        getIndexFiles(collectionPath).forEach(i -> {
            Document document = JSONUtilities.readJSONFile(
                    collectionPath + File.separator + i, Document.class);
            if(document != null)
                documents.add(document);
        });
        return documents;
    }

    private CollectionSchema readSchema(String collectionPath) {
        return JSONUtilities.readJSONFile(collectionPath + File.separator + "schema.collection",
                CollectionSchema.class);
    }

    private String getDatabasePath(int databaseIndex) {
        return databasesRootPath + File.separator + databaseIndex;
    }

    private String getCollectionPath(int collectionIndex, int databaseIndex) {
        return getDatabasePath(databaseIndex) + File.separator + collectionIndex;
    }

    private List<Integer> getIndexFiles(String path) {
        return IOUtils.getFileNames(new File(path),
                n -> Utils.NUMBERS_ONLY.matcher(n).find())
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

}
