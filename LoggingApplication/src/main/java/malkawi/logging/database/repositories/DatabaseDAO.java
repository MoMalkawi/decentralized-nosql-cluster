package malkawi.logging.database.repositories;

import java.util.List;

public interface DatabaseDAO<T> {

    List<T> getAll(int collectionId);

    List<T> getAll(String collectionName);

    T get(int documentId, int collectionId);

    T get(int documentId, String collectionName);

    T create(int collectionId, T object);

    T create(String collectionName, T object);

    boolean delete(String collectionName, int documentId);

    boolean update(int documentId, int collectionId, Object... valuePairs);

    boolean update(int documentId, String collectionId, Object... valuePairs);

    int createCollectionIfNotExist(String collectionName);

    boolean createDatabaseIfNotExist();

}
