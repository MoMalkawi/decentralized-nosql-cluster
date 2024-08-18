package malkawi.project.database.api.impl.service;

import malkawi.project.database.DBSystemSession;
import malkawi.project.database.components.collections.DocumentCollection;
import malkawi.project.database.components.collections.documents.Document;
import malkawi.project.database.data.Result;
import malkawi.project.database.data.ResultBuilder;
import malkawi.project.database.data.ResultRow;
import malkawi.project.net.client.impl.database.data.DatabaseServerInfo;

import java.util.List;
import java.util.Objects;

public class APIService {

    private final AffinityService affinityService;

    public APIService(DBSystemSession session) {
        this.affinityService = new AffinityService(session);
    }

    public Result getAllDocuments(DocumentCollection collection, String[] properties) {
        if(collection != null) {
            List<Document> documents = collection.getManager().getDocuments(Objects::nonNull);
            if (documents != null) {
                return new ResultBuilder()
                        .success()
                        .message("Fetched [" + documents.size() + "] documents.")
                        .rows(documents.stream()
                                .map(d -> new ResultRow(d.getId(),
                                        collection.getManager().getDocumentPropertyValues(d, properties)))
                                .toArray(ResultRow[]::new))
                        .build();
            }
        }
        return Result.FAILURE;
    }

    public Result getDocument(int documentId, DocumentCollection collection, String[] properties) {
        if(collection != null) {
            Document document = collection.getManager().getDocument(documentId);
            if(document != null)
                return new ResultBuilder()
                        .success()
                        .message("Fetched document (" + documentId + ")")
                        .row(new ResultRow(document.getId(),
                                        collection.getManager().getDocumentPropertyValues(document, properties)))
                        .build();
        }
        return Result.FAILURE;
    }

    public Result createDocument(DocumentCollection collection, Object[] propertyValues) {
        if (collection != null) {
            Document resultantDocument = collection.getManager().createDocument(propertyValues);
            return new ResultBuilder()
                    .success()
                    .message("Created document in collection [" + collection.getId() + "]")
                    .row(resultantDocument.getId())
                    .build();
        }
        return Result.FAILURE;
    }

    public Result updateDocument(int documentId, DocumentCollection collection, Object[] oldPropertyValues,
                                 Object[] propertyValues) {
        if(collection != null) {
            DatabaseServerInfo serverInfo;
            if((serverInfo = affinityService.getUpdateAffinityOwner(collection, documentId)) != null)
                return affinityService.redirectUpdateRequest(collection.getId(), documentId,
                        oldPropertyValues, propertyValues, serverInfo);
            Document resultantDocument = collection.getManager().updateDocument(documentId, oldPropertyValues, propertyValues);
            if (resultantDocument != null)
                return new ResultBuilder()
                        .success()
                        .message("Applied [" + (propertyValues.length / 2) + "] property updates into document.")
                        .build();
        }
        return Result.FAILURE;
    }

}
