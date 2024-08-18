package malkawi.project.database;

import lombok.Getter;
import lombok.Setter;
import malkawi.project.database.api.impl.DBSystemAPI;
import malkawi.project.database.components.databases.Database;
import malkawi.project.database.components.users.User;

@Getter
public class DBSystemSession {

    private final DBSystemAPI api;

    private final DBSystem system;

    private @Setter User user;

    private Database database;

    public DBSystemSession(DBSystem system) {
        this.system = system;
        this.api = new DBSystemAPI(this);
    }

    public void databaseLogin(String databaseName) {
        Database database = system.getManager().getDatabaseByName(databaseName);
        if(database != null && database.getManager().canAccess(user))
            this.database = database;
    }

}
