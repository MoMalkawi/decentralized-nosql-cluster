package malkawi.project.utilities.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

public class PropertiesLoader {

    private final boolean intelliJ;

    public PropertiesLoader(boolean intelliJ) {
        this.intelliJ = intelliJ;
    }
    public Properties loadProperties() {
        try(InputStream inputStream = getInputStream()) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private InputStream getInputStream() throws IOException {
        return intelliJ ?
                getClass().getClassLoader().getResourceAsStream("application.properties")
                :
                Files.newInputStream(new File("./application.properties").toPath());
    }

}
