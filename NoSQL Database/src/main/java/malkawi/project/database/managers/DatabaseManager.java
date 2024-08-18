package malkawi.project.database.managers;

import lombok.AllArgsConstructor;
import malkawi.project.database.components.collections.data.properties.Property;
import malkawi.project.database.components.databases.Database;
import malkawi.project.database.components.collections.DocumentCollection;
import malkawi.project.database.components.users.User;
import malkawi.project.utilities.interfaces.Filter;
import malkawi.project.utilities.io.console.Console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DatabaseManager {

    private final Database database;

    public synchronized List<DocumentCollection> insertCollections(List<DocumentCollection> collections) {
        collections.forEach(c -> insertCollection(c, false));
        database.notifySystem(false);
        return collections;
    }

    public synchronized DocumentCollection insertCollection(DocumentCollection collection) {
        return insertCollection(collection, true);
    }

    public synchronized DocumentCollection insertCollection(int collectionIndex, String collectionName, Object... schema) {
        DocumentCollection collection = new DocumentCollection(collectionIndex, collectionName);
        populateCollectionFromSchema(collection, schema);
        return insertCollection(collection, true);
    }

    private synchronized DocumentCollection insertCollection(DocumentCollection collection, boolean alertChanged) {
        if(database.getLastIndex() < collection.getId())
            database.setLastIndex(collection.getId());
        collection.setDatabaseId(database.getId());
        collection.setLive();
        database.getCollections().put(collection.getId(), collection);
        if(alertChanged) {
            database.notifySystem(false);
            collection.notifySystem(false);
        }
        return collection;
    }

    public synchronized List<DocumentCollection> createCollections(List<DocumentCollection> collections) {
        collections.forEach(c -> createCollection(c, false));
        database.notifySystem(true);
        return collections;
    }

    public synchronized DocumentCollection createCollection(String collectionName, Object... schema) {
        DocumentCollection collection = new DocumentCollection(-1, collectionName);
        populateCollectionFromSchema(collection, schema);
        return createCollection(collection);
    }

    public void populateCollectionFromSchema(DocumentCollection collection, Object... schema) {
        for(int i = 0; i < schema.length; i += 2) {
            try {
                collection.getManager().addProperty(new Property(
                        Class.forName((String) schema[i]),
                        (String)schema[i + 1]));
            } catch (ClassNotFoundException e) {
                Console.error("[ERROR] property addition syntax problem.\n" + e.getMessage());
            }
        }
    }

    public synchronized DocumentCollection createCollection(DocumentCollection collection) {
        return createCollection(collection, true);
    }

    private DocumentCollection createCollection(DocumentCollection collection, boolean alertChanged) {
        if(isCollectionExist(collection.getName()))
            return getCollection(c -> c.getName().equals(collection.getName()));
        int collectionIndex = generateCollectionId();
        collection.setId(collectionIndex);
        collection.setDatabaseId(database.getId());
        collection.setLive();
        database.getCollections().put(collectionIndex, collection);
        if(alertChanged) {
            database.notifySystem(true);
            collection.notifySystem(true);
        }
        return collection;
    }

    private int generateCollectionId() {
        int index = database.getLastIndex() + 1;
        database.setLastIndex(index);
        return index;
    }

    public synchronized List<DocumentCollection> removeCollections(Filter<DocumentCollection> collectionsFilter, boolean alertCluster) {
        List<DocumentCollection> collections = getCollections(collectionsFilter);
        if(collections != null) {
            collections.forEach(c -> removeCollection(c.getId(), false, alertCluster));
            database.notifySystem(true);
        }
        return collections;
    }

    public synchronized DocumentCollection removeCollection(Filter<DocumentCollection> collectionFilter, boolean alertCluster) {
        DocumentCollection collection = getCollection(collectionFilter);
        return collection != null ? removeCollection(collection.getId(), true, alertCluster) : null;
    }

    public synchronized List<DocumentCollection> removeCollections(int... indices) {
        List<DocumentCollection> collections = new ArrayList<>();
        Arrays.stream(indices).forEach(i -> {
            DocumentCollection collection = removeCollection(i, false, true);
            if(collection != null)
                collections.add(collection);
        });
        database.notifySystem(true);
        return collections;
    }

    private DocumentCollection removeCollection(int index, boolean alertUpdater, boolean alertCluster) {
        DocumentCollection collection = database.getCollections().get(index);
        if(collection != null) {
            database.getCollections().remove(collection.getId());
            if(alertUpdater)
                database.notifySystem(alertCluster);
        }
        return collection;
    }

    public boolean isCollectionExist(String name) {
        return getCollection(d -> d.getName().equalsIgnoreCase(name)) != null;
    }

    public List<DocumentCollection> getCollections(Filter<DocumentCollection> collectionFilter) {
        return database.getCollections()
                .values()
                .stream()
                .filter(c -> c != null && collectionFilter.verify(c))
                .collect(Collectors.toList());
    }

    public DocumentCollection getCollection(Filter<DocumentCollection> collectionFilter) {
        return database.getCollections()
                .values()
                .stream()
                .filter(c -> c != null && collectionFilter.verify(c))
                .findFirst()
                .orElse(null);
    }

    public List<DocumentCollection> getCollections(int... ids) {
        List<DocumentCollection> collections = new ArrayList<>();
        Arrays.stream(ids).forEach(id -> {
            DocumentCollection collection = getCollection(id);
            if(collection != null)
                collections.add(collection);
        });
        return collections;
    }

    public DocumentCollection getCollection(int id) {
        return database.getCollections().get(id);
    }

    public int getCollectionOffset(int collectionId) {
        int sum = 0;
        for(DocumentCollection collection : database.getCollections().values()) {
            if(collection.getId() != collectionId)
                sum += collection.getDocuments().size();
        }
        return sum;
    }

    public int getCollectionsDocumentCount() {
        int sum = 0;
        for(DocumentCollection collection : database.getCollections().values())
            sum += collection.getDocuments().size();
        return sum;
    }

    public boolean canAccess(User user) {
        return true; //TODO: (optional) do this later
    }

}