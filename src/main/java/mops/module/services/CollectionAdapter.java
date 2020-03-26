package mops.module.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Collection;

class CollectionAdapter implements JsonSerializer<Collection<?>> {

    @Override
    public JsonElement serialize(Collection<?> src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        if (src == null || src.isEmpty()) {
            return null;
        }

        JsonArray array = new JsonArray();

        for (Object child : src) {
            JsonElement element = context.serialize(child);
            array.add(element);
        }

        return array;
    }
}