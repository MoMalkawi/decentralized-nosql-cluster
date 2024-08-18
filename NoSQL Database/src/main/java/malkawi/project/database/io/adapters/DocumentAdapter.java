package malkawi.project.database.io.adapters;

import com.google.gson.*;
import malkawi.project.database.components.collections.documents.Document;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class DocumentAdapter implements JsonSerializer<Document>, JsonDeserializer<Document> {

    @Override
    public Document deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject documentObject = jsonElement.getAsJsonObject();
        Document document = new Document(documentObject.get("id").getAsInt());
        document.setCollectionId(documentObject.get("collectionId").getAsInt());
        List<JsonElement> dataArray = documentObject.get("data").getAsJsonArray().asList();
        for(JsonElement dataRaw : dataArray) {
            JsonObject data = dataRaw.getAsJsonObject();
            document.addValue(data.get("property_id").getAsInt(),
                    jsonDeserializationContext.deserialize(data.get("value"), Object.class));
        }
        return document;
    }

    @Override
    public JsonElement serialize(Document document, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", document.getId());
        result.addProperty("collectionId", document.getCollectionId());
        JsonArray array = new JsonArray();
        for(Map.Entry<Integer, Object> propertyValue : document.getValues().entrySet()) {
            JsonObject property = new JsonObject();
            property.addProperty("property_id", propertyValue.getKey());
            property.add("value", jsonSerializationContext.serialize(propertyValue.getValue()));
            array.add(property);
        }
        result.add("data", array);
        return result;
    }

}
