package mops.module.services;

import com.google.gson.Gson;
import mops.module.database.Modul;
import org.springframework.stereotype.Service;

@Service
public class JsonService {

    private final Gson gson = new Gson();

    public String modulToJsonObject(Modul modul) {
        return gson.toJson(modul);
    }

    public Modul jsonObjectToModul(String json) {
        return gson.fromJson(json, Modul.class);
    }
}
