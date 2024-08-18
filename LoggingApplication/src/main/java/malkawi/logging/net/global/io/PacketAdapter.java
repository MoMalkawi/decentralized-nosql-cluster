package malkawi.logging.net.global.io;

import com.google.gson.*;
import malkawi.logging.database.data.result.Result;
import malkawi.logging.database.data.users.User;
import malkawi.logging.net.global.data.Packet;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacketAdapter implements JsonSerializer<Packet>, JsonDeserializer<Packet> {

    private final Map<String, Class<?>> acceptedTypes = new HashMap<>();

    public PacketAdapter() {
        initAcceptedTypes();
    }

    @Override
    public Packet deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Packet packet = new Packet();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        packet.setTypeName(jsonObject.get("typeName").getAsString());
        packet.setIdentifier(jsonObject.get("identifier").getAsLong());
        packet.setContentTypes(jsonDeserializationContext.deserialize(jsonObject.get("contentTypes"), String[].class));
        List<Object> content = new ArrayList<>();
        List<JsonElement> contentArray = jsonObject.get("content").getAsJsonArray().asList();
        int index = 0;
        for(JsonElement element : contentArray) {
            content.add(jsonDeserializationContext.deserialize(element, getClassType(packet.getContentTypes()[index])));
            index++;
        }
        packet.setContent(content.toArray(new Object[0]));
        return packet;
    }

    @Override
    public JsonElement serialize(Packet packet, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("typeName", packet.getTypeName());
        result.addProperty("identifier", packet.getIdentifier());
        result.add("contentTypes", jsonSerializationContext.serialize(packet.getContentTypes()));
        JsonArray content = new JsonArray();
        for(Object object : packet.getContent())
            content.add(jsonSerializationContext.serialize(object));
        result.add("content", content);
        return result;
    }

    private Class<?> getClassType(String type) {
        if(!type.contains("."))
            return acceptedTypes.getOrDefault(type, Object.class);
        try {
            return Class.forName(type);
        } catch (ClassNotFoundException e) {
            return Object.class;
        }
    }

    private void initAcceptedTypes() {
        acceptedTypes.put("Result", Result.class);
        acceptedTypes.put("User", User.class);
    }

}
