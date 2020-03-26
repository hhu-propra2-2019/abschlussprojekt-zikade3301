package mops.module.services;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Collection;
import mops.module.database.Modul;
import mops.module.wrapper.ModulWrapper;
import org.springframework.stereotype.Service;

@Service
public class JsonService {

    static ExclusionStrategy strategy = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> skipClass) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return field.getAnnotation(JsonExclude.class) != null;
        }
    };
    private static final Gson gson = new GsonBuilder().setExclusionStrategies(strategy)
            .registerTypeHierarchyAdapter(Collection.class, new CollectionAdapter()).create();

    public static String modulToJsonObject(Modul modul) {
        return gson.toJson(modul);
    }

    public static String modulWrapperToJsonObject(ModulWrapper modulwrapper) {
        return gson.toJson(modulwrapper);
    }

    public static Modul jsonObjectToModul(String json) {
        return gson.fromJson(json, Modul.class);
    }
}
