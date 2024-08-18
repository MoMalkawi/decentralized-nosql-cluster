package malkawi.project.database.io.adapters;

import com.google.gson.*;
import malkawi.project.database.components.databases.Database;

import java.lang.reflect.Type;

/**
 *
 * @author Mohammad Malkawi
 * Serializes and Deserializes the Database MetaData file.
 */
public class DatabaseAdapter implements JsonSerializer<Database>, JsonDeserializer<Database> {

    @Override
    public JsonElement serialize(Database database, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", database.getId());
        result.addProperty("name", database.getName());
        result.addProperty("lastIndex", database.getLastIndex());
        return result;
    }

    @Override
    public Database deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject databaseMeta = jsonElement.getAsJsonObject();
        Database database = new Database(
                databaseMeta.get("id").getAsInt(),
                databaseMeta.get("name").getAsString());
        database.setLastIndex(databaseMeta.get("lastIndex").getAsInt());
        return database;
    }

}
