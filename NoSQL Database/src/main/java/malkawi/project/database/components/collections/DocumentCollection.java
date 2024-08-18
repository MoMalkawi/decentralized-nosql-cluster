package malkawi.project.database.components.collections;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import malkawi.project.database.components.collections.data.CollectionSchema;
import malkawi.project.database.components.collections.documents.Document;
import malkawi.project.database.components.collections.update.CollectionUpdater;
import malkawi.project.database.managers.CollectionManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

@Getter
@RequiredArgsConstructor
public class DocumentCollection extends Observable implements Serializable {

    private @Setter int id;

    private final @NonNull String name;

    private transient @NonNull @Setter CollectionSchema schema;

    private final transient Map<Integer, Document> documents = new HashMap<>();

    private final transient CollectionManager manager = new CollectionManager(this);

    private volatile @Setter int lastIndex = -1;

    private transient @Setter int databaseId;

    public DocumentCollection(int id, String name) {
        this.id = id;
        this.name = name;
        this.schema = new CollectionSchema();
    }

    public void notifySystem(boolean updateCluster) {
        setChanged();
        notifyObservers(updateCluster);
    }

    public void setLive() {
        if(countObservers() == 0)
            addObserver(new CollectionUpdater(this));
    }

}
