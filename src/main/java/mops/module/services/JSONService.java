package mops.module.services;

import com.google.gson.Gson;
import mops.module.database.Modul;
import org.springframework.stereotype.Service;

@Service
public class JSONService {

    private final Gson gson = new Gson();

    public String modulToJSONObject(Modul modul) {
        return gson.toJson(modul);
    }

    public Modul jsonObjectToModul(String json) {
        return gson.fromJson(json, Modul.class);
    }

    //public String calculateModulDiffs(Modul modul1, Modul modul2)

    //ModulID in Antrag
}
