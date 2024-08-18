package malkawi.project.database.io.adapters;

import com.google.gson.*;
import malkawi.project.database.components.collections.data.CollectionSchema;
import malkawi.project.database.components.collections.data.properties.Property;
import malkawi.project.utilities.io.console.Console;

import java.lang.reflect.Type;
import java.util.List;

public class SchemaAdapter implements
        JsonSerializer<CollectionSchema>,
        JsonDeserializer<CollectionSchema> {

    @Override
    public CollectionSchema deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject schemaObject = jsonElement.getAsJsonObject();
        CollectionSchema schema = new CollectionSchema();
        schema.setLastIndex(schemaObject.get("lastIndex").getAsInt());
        List<JsonElement> propertiesArray = schemaObject
                .getAsJsonArray("properties")
                .asList();
        for(JsonElement propertyRaw : propertiesArray) {
            JsonObject property = propertyRaw.getAsJsonObject();
            int id = property.get("id").getAsInt();
            try {
                schema.getProperties().put(id, new Property(
                        id,
                        Class.forName(property.get("type").getAsString()),
                        property.get("name").getAsString()));
            } catch (ClassNotFoundException e) {
                Console.error("[ERROR] couldn't deserialize property type at SchemaAdapter:deserialize.\n"
                        + e.getMessage());
            }
        }
        return schema;
    }

    @Override
    public JsonElement serialize(CollectionSchema collectionSchema, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        JsonArray properties = new JsonArray();
        for(Property property : collectionSchema.getProperties().values()) {
            JsonObject propertyObject = new JsonObject();
            propertyObject.addProperty("id", property.getIndex());
            propertyObject.addProperty("name", property.getName());
            propertyObject.addProperty("type", property.getType().getName());
            properties.add(propertyObject);
        }
        result.add("properties", properties);
        result.addProperty("lastIndex", collectionSchema.getLastIndex());
        return result;
    }

}
