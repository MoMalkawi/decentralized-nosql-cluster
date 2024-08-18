package malkawi.project.data;

import lombok.Getter;
import malkawi.project.net.global.data.ServerInfo;
import malkawi.project.utilities.io.PropertiesLoader;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Config {

    private static Config instance = null;

    private final @Getter String basePath;

    private final @Getter int bootstrapPort;

    private final @Getter String[] rawServerInfos;

    private final @Getter List<ServerInfo> serverInfos;

    private final @Getter String[] databases;

    private Config() {
        Properties properties = new PropertiesLoader(true).loadProperties();
        this.basePath = properties.getProperty("bootstrap.base_path");
        this.bootstrapPort = Integer.parseInt(properties.getProperty("bootstrap.port"));
        String rawHosts = properties.getProperty("cluster.hosts");
        this.rawServerInfos = rawHosts.contains(",") ? rawHosts.split(",") :
                Collections.singletonList(rawHosts).toArray(new String[0]);
        this.serverInfos = Arrays.stream(rawServerInfos).map(s -> {
            String[] splitHostData = s.split(":");
            return new ServerInfo(splitHostData[0], Integer.parseInt(splitHostData[1]));
        }).collect(Collectors.toList());
        String rawDatabases = properties.getProperty("cluster.databases");
        this.databases = rawDatabases.contains(",") ?
                rawDatabases.split(",") : Collections.singletonList(rawDatabases).toArray(new String[0]);
    }

    public static Config get() {
        if(instance == null)
            instance = new Config();
        return instance;
    }

}
