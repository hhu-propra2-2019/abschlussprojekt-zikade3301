package mops.module.services;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.repositories.AntragsRepository;
import mops.module.repositories.ModulSnapshotRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModulService {

    private final AntragsRepository antragsRepository;
    private final ModulSnapshotRepository modulSnapshotRepository;
    private final JSONService jsonService;

    /**
     * Wenn das Modul nicht existiert, wird es direkt als Antrag gespeichert
     * Wenn das Modul doch existiert, wird die difference als Antrag gespeichert
     * unf
     *
     * @param newModul
     * @param approveDate
     */
    public void addModul(Modul newModul, LocalDateTime approveDate) {
        Modul modul = modulSnapshotRepository.findById(newModul.getId()).get();
        if (modul != null) {
            modul = calculateModulDiffs(modul, newModul);
        }
        Antrag antrag = toAntrag(modul, approveDate);
        antragsRepository.save(antrag);
        return;


    }

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

    public Modul calculateModulDiffs(Modul altesmodul, Modul neuesmodul) {
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

            try {
                if (field.get(altesmodul) == null) {
                    field.set(aenderungen, field.get(neuesmodul));
                    continue;
                }
                if (!field.get(altesmodul).equals(field.get(neuesmodul))) {
                    field.set(aenderungen, field.get(neuesmodul));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return aenderungen;
    }

    //Modul buildModulUntil(List<Antrag> antraege, LocalDateTime when)
}
