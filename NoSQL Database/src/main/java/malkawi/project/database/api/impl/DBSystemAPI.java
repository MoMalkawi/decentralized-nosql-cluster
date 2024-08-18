package malkawi.project.database.api.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import malkawi.project.database.DBSystemSession;
import malkawi.project.database.api.DBAbstractAPI;
import malkawi.project.database.api.Mapping;
import malkawi.project.database.api.impl.service.APIService;
import malkawi.project.database.components.collections.DocumentCollection;
import malkawi.project.database.components.collections.documents.Document;
import malkawi.project.database.components.databases.Database;
import malkawi.project.database.data.Result;
import malkawi.project.database.data.ResultBuilder;

import java.util.Arrays;

@RequiredArgsConstructor
@SuppressWarnings("unused")
public class DBSystemAPI extends DBAbstractAPI {

    private final @NonNull DBSystemSession session;

    private final APIService service;

    public DBSystemAPI(DBSystemSession session) {
        this.session = session;
        this.service = new APIService(session);
    }

    @Mapping("create-db")
    public Result createDatabase(String databaseName) {
        if(session.getSystem().getUserManager().isRoot(session.getUser())) {
            Database database = session.getSystem().getManager().createDatabase(databaseName);
            return new ResultBuilder()
                    .success()
                    .message("Database created.")
                    .row(database.getId(), database.getName())
                    .build();
        }
        return Result.AUTH_FAILURE;
    }

    @Mapping("create-collection")
    public Result createCollection(String collectionName, Object[] schema) {
        DocumentCollection collectionResult = session.getDatabase().getManager()
                .createCollection(collectionName, schema);
        return new ResultBuilder()
                .success()
                .message("Collection created in database [" + session.getDatabase().getId() + "]")
                .row(collectionResult.getId(), collectionResult.getName())
                .build();
    }

    @Mapping("insert-collection")
    public Result insertCollection(int collectionId, String collectionName, Object[] schema) {
        DocumentCollection collectionResult = session.getDatabase().getManager()
                .insertCollection(collectionId, collectionName, schema);
        return new ResultBuilder()
                .success()
                .message("Collection inserted in database [" + session.getDatabase().getId() + "]")
                .row(collectionResult.getId(), collectionResult.getName())
                .build();
    }

    @Mapping("remove-collection")
    public Result removeCollection(String collectionName) {
        DocumentCollection removalResult = session.getDatabase().getManager()
                .removeCollection(c -> c.getName().equalsIgnoreCase(collectionName), true);
        if(removalResult != null) {
            return new ResultBuilder()
                    .success()
                    .message("Collection removed from database [" + session.getDatabase().getId() + "]")
                    .row(removalResult.getId(), removalResult.getName())
                    .build();
        }
        return Result.FAILURE;
    }

    @Mapping("remove-collection-nodal")
    public Result removeCollection(int collectionId) {
        DocumentCollection removalResult = session.getDatabase().getManager()
                .removeCollection(c -> c.getId() == collectionId, true);
        if(removalResult != null) {
            return new ResultBuilder()
                    .success()
                    .message("Collection removed from database [" + session.getDatabase().getId() + "]")
                    .row(removalResult.getId(), removalResult.getName())
                    .build();
        }
        return Result.FAILURE;
    }

    @Mapping("create-document-col-id")
    public Result createDocument(int collectionId, Object[] propertyValues) {
        DocumentCollection collection = session.getDatabase().getManager().getCollection(collectionId);
        return service.createDocument(collection, propertyValues);
    }

    @Mapping("create-document-col-name")
    public Result createDocument(String collectionName, Object[] propertyValues) {
        DocumentCollection collection = session.getDatabase().getManager()
                .getCollection(d -> d != null && d.getName().equals(collectionName));
        return service.createDocument(collection, propertyValues);
    }

    @Mapping("insert-document")
    public Result insertDocument(int collectionId, int documentId, Object[] propertyValues) {
        DocumentCollection collection = session.getDatabase().getManager().getCollection(collectionId);
        Document document = collection.getManager().insertDocument(documentId, propertyValues);
        if(document != null)
            return new ResultBuilder()
                    .success()
                    .message("Inserted document in collection [" + collection.getId() + "]")
                    .row(document.getId())
                    .build();
        return Result.FAILURE;
    }

    @Mapping("remove-document")
    public Result removeDocument(String collectionName, int documentId) {
        DocumentCollection collection = session.getDatabase().getManager()
                .getCollection(d -> d != null && d.getName().equals(collectionName));
        Document document = collection.getManager().removeDocument(documentId, true, true);
        if(document != null)
            return new ResultBuilder()
                    .success()
                    .message("Deleted document in collection [" + collection.getId() + "]")
                    .row(document.getId())
                    .build();
        return Result.FAILURE;
    }

    @Mapping("remove-document-nodal")
    public Result removeDocument(int collectionId, int documentId) {
        DocumentCollection collection = session.getDatabase().getManager().getCollection(collectionId);
        Document document = collection.getManager().removeDocument(documentId, true, false);
        if(document != null)
            return new ResultBuilder()
                    .success()
                    .message("Deleted document in collection [" + collection.getId() + "]")
                    .row(document.getId())
                    .build();
        return Result.FAILURE;
    }

    @Mapping("update-document-col-id")
    public Result updateDocument(int documentId, int collectionId,
                                 Object[] oldPropertyPairs,
                                 Object[] propertyPairs) {
        DocumentCollection collection = session.getDatabase().getManager().getCollection(collectionId);
        return service.updateDocument(documentId, collection, oldPropertyPairs, propertyPairs);
    }

    @Mapping("update-document-col-name")
    public Result updateDocument(int documentId, String collectionName,
                                 Object[] oldPropertyPairs,
                                 Object[] propertyPairs) {
        DocumentCollection collection = session.getDatabase().getManager()
                .getCollection(d -> d != null && d.getName().equals(collectionName));
        return service.updateDocument(documentId, collection, oldPropertyPairs, propertyPairs);
    }

    @Mapping("document-col-id")
    public Result getDocument(int documentId, int collectionId, String[] properties) {
        DocumentCollection collection = session.getDatabase().getManager().getCollection(collectionId);
        return service.getDocument(documentId, collection, properties);
    }

    @Mapping("document-col-name")
    public Result getDocument(int documentId, String collectionName, String[] properties) {
        DocumentCollection collection = session.getDatabase().getManager()
                .getCollection(d -> d != null && d.getName().equals(collectionName));
        return service.getDocument(documentId, collection, properties);
    }

    @Mapping("all-documents-col-id")
    public Result getAllDocuments(int collectionId, String[] properties) {
        DocumentCollection collection = session.getDatabase().getManager().getCollection(collectionId);
        return service.getAllDocuments(collection, properties);
    }

    @Mapping("all-documents-col-name")
    public Result getAllDocuments(String collectionName, String[] properties) {
        DocumentCollection collection = session.getDatabase().getManager()
                .getCollection(d -> d != null && d.getName().equals(collectionName));
        return service.getAllDocuments(collection, properties);
    }

}
