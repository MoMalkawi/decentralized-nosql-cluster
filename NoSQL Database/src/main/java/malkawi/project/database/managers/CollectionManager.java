package malkawi.project.database.managers;

import lombok.AllArgsConstructor;
import malkawi.project.database.components.collections.documents.Document;
import malkawi.project.database.components.collections.DocumentCollection;
import malkawi.project.database.components.collections.data.properties.Property;
import malkawi.project.utilities.interfaces.Filter;
import malkawi.project.utilities.io.console.Console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CollectionManager {

    private final DocumentCollection collection;

    public synchronized List<Document> insertDocuments(List<Document> documents) {
        documents.forEach(d -> insertDocument(d, false));
        collection.notifySystem(false);
        return documents;
    }

    public synchronized Document insertDocument(int documentId, Object... propertyValue) {
        Document document = new Document(documentId);
        addRawPropertyValues(propertyValue, document);
        document.setChanged(true);
        return insertDocument(document, true);
    }

    public synchronized Document insertDocument(Document document) {
        return insertDocument(document, true);
    }

    private synchronized Document insertDocument(Document document, boolean alertObservers) {
        if(collection.getLastIndex() < document.getId())
            collection.setLastIndex(document.getId());
        document.setCollectionId(collection.getId());
        collection.getDocuments().put(document.getId(), document);
        if(alertObservers) {
            collection.getManager().getDocument(document.getId());
            collection.notifySystem(false);
        }
        return document;
    }

    public synchronized Document updateDocument(int index, Object[] oldPropertyValues, Object[] propertyPairs) {
        return updateDocument(index, d -> {
            Object[] currentPropertyValues = getRawPropertyValues(d);
            boolean optimisticCheck = true;
            for(int i = 0; i < currentPropertyValues.length; i++) {
                if(!currentPropertyValues[i].equals(oldPropertyValues[i])) {
                    optimisticCheck = false;
                    Console.info("[RACE] Race condition caught by Optimistic Lock!");
                    break;
                }
            }
            if(optimisticCheck)
                addRawPropertyValues(propertyPairs, d);
        });
    }

    public synchronized Document updateDocument(int index, Consumer<Document> updateFunction) {
        Document document = getDocument(index);
        if(document != null) {
            updateFunction.accept(document);
            document.setChanged(true);
            collection.notifySystem(true);
            return document;
        }
        return null;
    }

    public synchronized List<Document> createDocuments(List<Document> documents) {
        documents.forEach(d -> createDocument(d, false));
        collection.notifySystem(true);
        return documents;
    }

    public synchronized Document createDocument(Object... propertyValues) {
        Document document = new Document();
        System.out.println(Arrays.toString(propertyValues));
        addRawPropertyValues(propertyValues, document);
        return createDocument(document);
    }

    private void addRawPropertyValues(Object[] propertyValues, Document document) {
        for(int i = 0; i < propertyValues.length; i += 2) {
            if(propertyValues[i] instanceof String) {
                String propertyName = (String) propertyValues[i];
                Property property = getProperty(p -> p != null &&
                        p.getName().equalsIgnoreCase(propertyName));
                if(property != null)
                    document.addValue(property.getIndex(), propertyValues[i + 1]);
            }
        }
    }

    public Object[] getRawPropertyValues(Document document) {
        List<Object> result = new ArrayList<>();
        document.getValues().forEach((key, value) -> {
            Property property = getProperty(key);
            if (property != null) {
                result.add(property.getName());
                result.add(value);
            }
        });
        return result.toArray(new Object[0]);
    }

    public synchronized Document createDocument(Document document) {
        return createDocument(document, true);
    }

    private Document createDocument(Document document, boolean alertObservers) {
        int index = generateDocumentId();
        document.setId(index);
        document.setCollectionId(collection.getId());
        document.setChanged(true);
        collection.getDocuments().put(index, document);
        if(alertObservers)
            collection.notifySystem(true);
        return document;
    }

    public Object[] getDocumentPropertyValues(Document document, String... properties) {
        List<Object> result = new ArrayList<>();
        for(String propertyName : properties) {
            Property property = getProperty(p -> p != null && p.getName().equalsIgnoreCase(propertyName));
            if(property != null) {
                Object value = document.getValues().get(property.getIndex());
                if(value != null)
                    result.add(value);
            }
        }
        return result.toArray(new Object[0]);
    }

    public synchronized Document removeDocument(int documentId, boolean alertUpdater, boolean alertCluster) {
        Document document = collection.getDocuments().remove(documentId);
        if(document != null && alertUpdater)
            collection.notifySystem(alertCluster);
        return document;
    }

    private int generateDocumentId() {
        int index = collection.getLastIndex() + 1;
        collection.setLastIndex(index);
        return index;
    }

    public synchronized int removePropertyValues(Property property) {
        return removePropertyValues(property.getIndex());
    }

    public synchronized int removePropertyValues(int propertyIndex) {
        AtomicInteger deleted = new AtomicInteger();
        collection.getDocuments().values().forEach(d -> {
            if(d.removeValue(propertyIndex) != null)
                deleted.getAndIncrement();
        });
        int deletedCount = deleted.get();
        if(deletedCount > 0)
            collection.getSchema().setChanged(true);
        return deletedCount;
    }

    public synchronized Property addProperty(Property property) {
        int index = generatePropertyId();
        property.setIndex(index);
        collection.getSchema().getProperties().put(index, property);
        collection.notifySystem(true);
        collection.getSchema().setChanged(true);
        return property;
    }

    public List<Document> getDocuments(Filter<Document> documentFilter) {
        return collection.getDocuments().values().stream()
                .filter(documentFilter::verify)
                .collect(Collectors.toList());
    }

    public Document getDocument(int index) {
        return collection.getDocuments().get(index);
    }

    public List<Property> getProperties(Filter<Property> propertyFilter) {
        return collection.getSchema().getProperties()
                .values().stream()
                .filter(p -> p != null && propertyFilter.verify(p))
                .collect(Collectors.toList());
    }

    public Property getProperty(int index) {
        return collection.getSchema().getProperties().get(index);
    }

    public Property getProperty(Filter<Property> propertyFilter) {
        return collection.getSchema().getProperties()
                .values()
                .stream()
                .filter(p -> p != null && propertyFilter.verify(p))
                .findFirst()
                .orElse(null);
    }

    private int generatePropertyId() {
        int index = collection.getSchema().getLastIndex() + 1;
        collection.getSchema().setLastIndex(index);
        return index;
    }

}
