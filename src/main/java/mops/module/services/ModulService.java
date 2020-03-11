package mops.module.services;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;
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
    private final JsonService jsonService;

    /**
     * Wenn das Modul nicht existiert, wird es direkt als Antrag gespeichert.
     * Wenn das Modul doch existiert, wird die difference als Antrag gespeichert.
     *
     * @param newModul    Neues Modul
     * @param approveDate Datum der Annahme
     */
    public void addModul(Modul newModul, LocalDateTime approveDate) {
        Optional<Modul> optionalModul = modulSnapshotRepository.findById(newModul.getId());
        Modul diffModul = newModul;
        if (optionalModul.isPresent()) {
            diffModul = calculateModulDiffs(optionalModul.get(), newModul);
        }
        Antrag antrag = toAntrag(diffModul, approveDate);
        antragsRepository.save(antrag);
        return;


    }

    /**
     * Erstellt aus einem Modul ein Antrag.
     *
     * @param modul Modul auf welches der Antrag angewendet wird
     * @return
     */
    Antrag toAntrag(Modul modul, LocalDateTime approveDate) {
        String jsonObject = jsonService.modulToJsonObject(modul);
        Antrag antrag = new Antrag();
        antrag.setModul(jsonObject);
        antrag.setApproveDate(approveDate);
        return antrag;

    }

    /**
     * Berechnet die Differenzen zwischen zwei Modulen.
     *
     * @param altesmodul Altes Modul
     * @param neuesmodul Neues Modul
     * @return
     */
    public Modul calculateModulDiffs(Modul altesmodul, Modul neuesmodul) {
        Modul aenderungen = new Modul();
        boolean foundDiffs = false;

        if (altesmodul == null || neuesmodul == null) {
            throw new IllegalArgumentException("Ein Modul ist null!");
        }

        aenderungen.setId(neuesmodul.getId());

        for (Field field : neuesmodul.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            if ("datumAenderung".equals((String) field.getName())) {
                continue;
            }

            try {
                if (field.get(altesmodul) == null) {
                    if (field.get(neuesmodul) != null) {
                        field.set(aenderungen, field.get(neuesmodul));
                        foundDiffs = true;
                    }
                    continue;
                }
                if (!field.get(altesmodul).equals(field.get(neuesmodul))) {
                    field.set(aenderungen, field.get(neuesmodul));
                    foundDiffs = true;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (!foundDiffs) {
            return null;
        }
        return aenderungen;
    }

    void applyAntragOnModul(Modul modul, Antrag antrag) {
        Modul modulaenderungen = jsonService.jsonObjectToModul(antrag.getModul());

        for (Field field : modul.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                if (field.get(modulaenderungen) == null) {
                    continue;
                }
                if (!field.get(modulaenderungen).equals(field.get(modul))) {
                    field.set(modul, field.get(modulaenderungen));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    //Modul buildModulUntil(List<Antrag> antraege, LocalDateTime when)
}
