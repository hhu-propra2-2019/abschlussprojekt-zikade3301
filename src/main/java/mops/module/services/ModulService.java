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

    /*Modul getModulUntil(Long modulid, LocalDateTime when) {
        List<Antrag> antraege = getAntraegeForModulUntil(modulid, when);
        return buildModulFromAntraege(antraege);
    }

    List<Antrag> getAntraegeForModulUntil(Long modulid, LocalDateTime when) {
        List<Antrag> antraege = antragRepository.findByModulid(modulid);
        if (antraege == null || antraege.isEmpty()) {
            return null;
        }
        return filterAntraegeUntil(antraege, when);
    }

    List<Antrag> filterAntraegeUntil(List<Antrag> antraege, LocalDateTime when) {
        List<Antrag> filteredantraege = new ArrayList<Antrag>();

        for (Antrag antrag : antraege) {
            if (antrag.getApproveDate().isBefore(when)) {
                filteredantraege.add(antrag);
            }
        }

        return filteredantraege;
    }

    Modul buildModulFromAntraege(List<Antrag> antraege) {
        if (antraege == null || antraege.isEmpty()) {
            return null;
        }

        Modul modul = new Modul();

        Long modulid = antraege.get(0).getModulid();
        for (Antrag antrag : antraege) {
            applyAntragOnModul(modul, antrag);
        }
        return modul;
    }*/

}
