package mops.module.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.repositories.AntragRepository;
import mops.module.repositories.ModulSnapshotRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModulService {

    private final AntragRepository antragRepository;
    private final ModulSnapshotRepository modulSnapshotRepository;


    /**
     * Berechnet die Differenzen zwischen zwei Modulen.
     *
     * @param altesmodul Altes Modul
     * @param neuesmodul Neues Modul
     * @return
     */
    public static Modul calculateModulDiffs(Modul altesmodul, Modul neuesmodul) {
        Modul aenderungen = new Modul();
        boolean foundDiffs = false;

        if (altesmodul == null || neuesmodul == null) {
            throw new IllegalArgumentException("Ein Modul ist null!");
        }

        aenderungen.setId(altesmodul.getId());

        for (Field field : neuesmodul.getClass().getDeclaredFields()) {
            if (checkSingleField(altesmodul, neuesmodul, aenderungen, field)) {
                foundDiffs = true;
            }
        }
        if (!foundDiffs) {
            return null;
        }
        return aenderungen;
    }

    private static boolean checkSingleField(Modul altesmodul, Modul neuesmodul,
                                            Modul aenderungen, Field field) {
        field.setAccessible(true);

        if ("datumAenderung".equals((String) field.getName())) {
            return false;
        }

        try {
            if (field.get(altesmodul) == null) {
                if (field.get(neuesmodul) != null) {
                    field.set(aenderungen, field.get(neuesmodul));
                    return true;
                }
            } else if (!field.get(altesmodul).equals(field.get(neuesmodul))) {
                field.set(aenderungen, field.get(neuesmodul));
                return true;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }


    static void applyAntragOnModul(Modul modul, Antrag antrag) {
        Modul modulaenderungen = JsonService.jsonObjectToModul(antrag.getJsonModulAenderung());

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

    public List<Modul> getAlleModule() {
        return StreamSupport.stream(modulSnapshotRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<Modul> getAlleSichtbarenModule() {
        return getAlleModule().stream().filter(Modul::getSichtbar).collect(Collectors.toList());
    }

}
