package mops.module.services;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.repositories.AntragsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModulService {

    private final AntragsRepository antragsRepository;
    private final JSONService jsonService;

    /**
     * Erstellt aus einem Modul ein Antrag
     *
     * @param modul
     * @return
     */
    Antrag toAntrag(Modul modul, LocalDateTime localDateTime) {
        String jsonObject = jsonService.modulToJSONObject(modul);
        Antrag antrag = new Antrag();
        antrag.setModul(jsonObject);
        return antrag;

    }

    public Modul calculateModulDiffs(Modul altesmodul, Modul neuesmodul) throws IllegalAccessException {
        Modul aenderungen = new Modul();
        aenderungen.setId(neuesmodul.getId());

        for (Field field : neuesmodul.getClass().getDeclaredFields()) {
            if (field == null) {
                continue;
            }

            field.setAccessible(true);

            if ("datumAenderung".equals((String) field.getName())) {
                continue;
            }

            if (field.get(altesmodul) == null) {
                field.set(aenderungen, field.get(neuesmodul));
                continue;
            }


            if (!field.get(altesmodul).equals(field.get(neuesmodul))) {
                field.set(aenderungen, field.get(neuesmodul));
            }
        }
        return aenderungen;
    }

    //Modul buildModulUntil(List<Antrag> antraege, LocalDateTime when)
}
