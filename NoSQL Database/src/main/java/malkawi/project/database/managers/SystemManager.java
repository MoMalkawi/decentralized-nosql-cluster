package malkawi.project.database.managers;

import lombok.AllArgsConstructor;
import malkawi.project.database.DBSystem;
import malkawi.project.database.components.databases.Database;

@AllArgsConstructor
public class SystemManager {

    private final DBSystem system;

    public synchronized Database insertDatabaseIfNotExist(Database database) {
        Database existingDatabase;
        if((existingDatabase = getDatabaseByName(database.getName())) != null)
            return existingDatabase;
        return insertDatabase(database);
    }

    public synchronized Database insertDatabase(Database database) {
        if(database == null)
            return null;
        if(database.getId() > system.getLastIndex())
            system.setLastIndex(database.getId());
        database.setLive();
        system.getDatabases().put(database.getId(), database);
        database.notifySystem(false);
        return database;
    }

    public synchronized Database createDatabase(String name) {
        Database database;
        if((database = getDatabaseByName(name)) != null)
            return database;
        int index = generateDatabaseId();
        database = new Database(index, name);
        database.setLive();
        system.getDatabases().put(index, database);
        database.notifySystem(true);
        return database;
    }

    public Database getDatabase(int id) {
        return system.getDatabases().get(id);
    }

    public Database getDatabaseByName(String name) {
        return system.getDatabases()
                .values()
                .stream()
                .filter(d -> d != null && d.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    private int generateDatabaseId() {
        int index = system.getLastIndex() + 1;
        system.setLastIndex(index);
        return index;
    }

}
