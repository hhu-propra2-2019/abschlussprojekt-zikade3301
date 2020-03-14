package mops.module.services;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Collection;
import mops.module.database.Modul;
import org.springframework.stereotype.Service;

@Service
public class JsonService {

    ExclusionStrategy strategy = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return field.getAnnotation(JsonExclude.class) != null;
        }
    };
    private final Gson gson = new GsonBuilder().setExclusionStrategies(strategy)
            .registerTypeHierarchyAdapter(Collection.class, new CollectionAdapter()).create();

    public String modulToJsonObject(Modul modul) {
        return gson.toJson(modul);
    }

    public Modul jsonObjectToModul(String json) {
        return gson.fromJson(json, Modul.class);
    }
}
