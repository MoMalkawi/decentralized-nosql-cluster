package malkawi.project.database.components.databases;

import lombok.Getter;
import lombok.Setter;
import malkawi.project.database.components.collections.DocumentCollection;
import malkawi.project.database.components.databases.update.DatabaseUpdater;
import malkawi.project.database.managers.DatabaseManager;

import java.io.Serializable;
import java.util.*;

@Getter @Setter
public class Database extends Observable implements Serializable {

    private final int id;

    private final String name;

    private final transient Map<Integer, DocumentCollection> collections = new HashMap<>();

    private final transient DatabaseManager manager = new DatabaseManager(this);

    private volatile int lastIndex = -1;

    public Database(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void notifySystem(boolean updateCluster) {
        setChanged();
        notifyObservers(updateCluster);
    }

    public void setLive() {
        if(countObservers() == 0)
            addObserver(new DatabaseUpdater(this));
    }

}