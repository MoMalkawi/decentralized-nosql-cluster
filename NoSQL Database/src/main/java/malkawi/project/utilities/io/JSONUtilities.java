package malkawi.project.utilities.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import malkawi.project.database.components.databases.Database;
import malkawi.project.database.components.collections.DocumentCollection;
import malkawi.project.database.components.collections.data.CollectionSchema;
import malkawi.project.database.components.collections.documents.Document;
import malkawi.project.database.io.adapters.*;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.io.PacketAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;

public class JSONUtilities {

    private static final Gson GSON;

    static {
        GSON = new GsonBuilder()
                .registerTypeAdapter(Database.class, new DatabaseAdapter())
                .registerTypeAdapter(DocumentCollection.class, new CollectionAdapter())
                .registerTypeAdapter(CollectionSchema.class, new SchemaAdapter())
                .registerTypeAdapter(Document.class, new DocumentAdapter())
                .registerTypeAdapter(LocalDate.class, new DateAdapter())
                .registerTypeAdapter(Packet.class, new PacketAdapter())
                .setPrettyPrinting()
                .create();
    }

    public static void overwriteJSONFile(Object object, File file) {
        IOUtils.writeFile(convertToJson(object), file, false);
    }

    public static <T> T readJSONFile(String path, Class<T> type) {
        String json = IOUtils.fileToString(path);
        return json != null ? jsonToObject(json, type) : null;
    }

    public static <T> T readJSONFile(String path, Type type) {
        String json = IOUtils.fileToString(path);
        return json != null ? jsonToObject(json, type) : null;
    }

    public static <T> T jsonToObject(String json, Class<T> classType) {
        return GSON.fromJson(json, classType);
    }

    public static <T> T jsonToObject(String json, Type classType) {
        return GSON.fromJson(json, classType);
    }

    public static String convertToJson(Object object) {
        return GSON.toJson(object);
    }

    public static JsonElement objectToGenericGson(Object object) {
        return new Gson().toJsonTree(object);
    }

    private JSONUtilities() {}

}
