package malkawi.logging.database.repositories.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import malkawi.logging.database.DatabaseSession;
import malkawi.logging.database.data.result.Result;
import malkawi.logging.database.data.result.ResultRow;
import malkawi.logging.database.repositories.DatabaseDAO;
import malkawi.logging.database.entities.LogInfo;
import malkawi.logging.database.requests.DatabaseRequester;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class LogInfoDAOImpl implements DatabaseDAO<LogInfo> {

    private final @NonNull DatabaseSession session;

    @Override
    public List<LogInfo> getAll(String collectionName) {
        return getAll(collectionName, false);
    }

    @Override
    public List<LogInfo> getAll(int collectionId) {
        return getAll(collectionId, true);
    }

    private List<LogInfo> getAll(Object collectionIdentifier, boolean intIdentifier) {
        List<LogInfo> logInfos = new ArrayList<>();
        Result result = DatabaseRequester.request(session,
                intIdentifier ? "all-documents-col-id" : "all-documents-col-name",
                collectionIdentifier, new String[] { "message", "date" });
        if(result != null && result.isSuccess()) {
            while(result.hasNext()) {
                ResultRow row = result.next();
                List<Object> propertyValues = row.getList(1);
                logInfos.add(new LogInfo(row.getInt(0),
                        (String) propertyValues.get(0), row.getDateFromList(1, 1)));
            }
        }
        return logInfos;
    }

    @Override
    public LogInfo get(int documentId, int collectionId) {
        return get(documentId, collectionId, true);
    }

    @Override
    public LogInfo get(int documentId, String collectionName) {
        return get(documentId, collectionName, false);
    }

    private LogInfo get(int documentId, Object collectionIdentifier, boolean intIdentifier) {
        Result result = DatabaseRequester.request(session,
                intIdentifier ? "document-col-id" : "document-col-name",
                documentId, collectionIdentifier,
                new String[] { "message", "date"});
        if(result != null && result.isSuccess() && result.hasNext()) {
            ResultRow row = result.next();
            return new LogInfo(row.getInt(0),
                    (String) row.getList(1).get(0), row.getDateFromList(1, 1));
        }
        return null;
    }

    @Override
    public LogInfo create(int collectionId, LogInfo object) {
        return create(collectionId, true, object);
    }

    @Override
    public LogInfo create(String collectionName, LogInfo object) {
        return create(collectionName, false, object);
    }

    @Override
    public boolean delete(String collectionName, int documentId) {
        Result result = DatabaseRequester.request(session, "remove-document",
                collectionName, documentId);
        return result != null && result.isSuccess();
    }

    private LogInfo create(Object collectionIdentifier, boolean intIdentifier, LogInfo object) {
        Result result = DatabaseRequester.request(session,
                intIdentifier ? "create-document-col-id" : "create-document-col-name",
                collectionIdentifier,
                new Object[] { "message", object.getMessage(), "date", object.getDate() });
        if (result != null && result.isSuccess() && result.hasNext()) {
            double index = (double) result.next().get(0);
            object.setId((int) index);
            return object;
        }
        return null;
    }

    @Override
    public boolean update(int documentId, int collectionId, Object... valuePairs) {
        return update(documentId, collectionId, true, valuePairs);
    }

    @Override
    public boolean update(int documentId, String collectionName, Object... valuePairs) {
        return update(documentId, collectionName, false, valuePairs);
    }

    private boolean update(int documentId,
                           Object collectionIdentifier, boolean intIdentifier,
                           Object... valuePairs) { // "id", 1, "message", "this is a message"
        Object[] oldValuePairs = getDocumentValuePairs(get(documentId, collectionIdentifier, intIdentifier));
        Result result = DatabaseRequester.request(session,
                intIdentifier ? "update-document-col-id" : "update-document-col-name",
                documentId, collectionIdentifier, oldValuePairs, valuePairs);
        return result != null && result.isSuccess();
    }

    private Object[] getDocumentValuePairs(LogInfo logInfo) {
        return logInfo != null ? new Object[] { "message", logInfo.getMessage(), "date", logInfo.getDate() } : null;
    }

    @Override
    public int createCollectionIfNotExist(String collectionName) {
        Result result = DatabaseRequester.request(session, "create-collection",
                collectionName,
                new Object[] { String.class.getName(), "message", LocalDate.class.getName(), "date" });
        if(result != null && result.isSuccess() && result.hasNext()) {
            double index = (double) result.next().get(0);
            return (int) index;
        }
        return -1;
    }

    @Override
    public boolean createDatabaseIfNotExist() {
        if(session.isDatabaseLogged())
            return true;
        Result result = DatabaseRequester.request(session,
                "create-db", "logs");
        if(result != null && result.isSuccess() && result.hasNext()) {
            session.loginDatabase("logs");
            return true;
        }
        return false;
    }

}
