package malkawi.project.utilities.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.io.PacketAdapter;

import java.io.File;
import java.lang.reflect.Type;

public class JSONUtilities {

    private static final Gson GSON;

    static {
        GSON = new GsonBuilder()
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
