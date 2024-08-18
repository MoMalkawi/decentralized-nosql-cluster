package malkawi.project.data;

import lombok.Getter;
import lombok.Setter;
import malkawi.project.utilities.io.PropertiesLoader;

import java.util.Properties;

public class Config {

    private static Config config = null;

    private @Setter @Getter String databasesRootPath;

    private final @Getter int bootStrapPort;

    private final @Getter String bootstrapHost;

    private @Setter @Getter int databasePort;

    private final @Getter String defaultDatabase;

    private Config() {
        Properties properties = new PropertiesLoader(true).loadProperties();
        databasesRootPath = properties.getProperty("databases.root.path");
        bootstrapHost = properties.getProperty("cluster.bootstrapHost");
        defaultDatabase = properties.getProperty("cluster.default_database");
        bootStrapPort = Integer.parseInt(properties.getProperty("cluster.bootstrap.port"));
        databasePort = Integer.parseInt(properties.getProperty("databases.port"));
    }

    public static Config get() {
        if(config == null)
            config = new Config();
        return config;
    }

}
