package malkawi.project.database;

import lombok.Getter;
import lombok.Setter;
import malkawi.project.data.Config;
import malkawi.project.database.components.databases.Database;
import malkawi.project.database.io.loaders.DatabasesLoader;
import malkawi.project.database.managers.SystemManager;
import malkawi.project.database.managers.UserManager;

import java.util.HashMap;
import java.util.Map;

@Getter
public class DBSystem {

    private final Map<Integer, Database> databases = new HashMap<>();

    private final SystemManager manager;

    private final UserManager userManager;

    private volatile @Setter int lastIndex = -1;

    private final String systemPath;

    public DBSystem() {
        this.systemPath = Config.get().getDatabasesRootPath();
        this.manager = new SystemManager(this);
        this.userManager = new UserManager();
        init();
    }

    private void init() {
        new DatabasesLoader(systemPath)
                .readDatabases().forEach(manager::insertDatabase);
    }

}
