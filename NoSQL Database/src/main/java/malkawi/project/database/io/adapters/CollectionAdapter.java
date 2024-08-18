package malkawi.project.database.io.adapters;

import com.google.gson.*;
import malkawi.project.database.components.collections.DocumentCollection;

import java.lang.reflect.Type;

public class CollectionAdapter implements
        JsonSerializer<DocumentCollection>,
        JsonDeserializer<DocumentCollection> {

    @Override
    public DocumentCollection deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject collectionMeta = jsonElement.getAsJsonObject();
        DocumentCollection collection = new DocumentCollection(
                collectionMeta.get("id").getAsInt(),
                collectionMeta.get("name").getAsString());
        collection.setDatabaseId(collectionMeta.get("databaseId").getAsInt());
        collection.setLastIndex(collectionMeta.get("lastIndex").getAsInt());
        return collection;
    }

    @Override
    public JsonElement serialize(DocumentCollection collection, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", collection.getId());
        result.addProperty("name", collection.getName());
        result.addProperty("databaseId", collection.getDatabaseId());
        result.addProperty("lastIndex", collection.getLastIndex());
        return result;
    }

}
