package mops.module.services;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import mops.module.controller.ModulWrapper;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.database.Veranstaltung;
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
     * @param oldModul Altes Modul
     * @param newModul Neues Modul
     * @return
     */
    public static Modul calculateModulDiffs(Modul oldModul, Modul newModul) {
        Modul changes = new Modul();
        boolean foundDiffs = false;

        if (oldModul == null || newModul == null) {
            throw new IllegalArgumentException("Mindestens ein Modul ist null!");
        }

        changes.setId(oldModul.getId());

        for (Field field : newModul.getClass().getDeclaredFields()) {
            if (checkSingleField(oldModul, newModul, changes, field)) {
                foundDiffs = true;
            }
        }
        if (!foundDiffs) {
            return null;
        }
        return changes;
    }

    private static boolean checkSingleField(Modul altesmodul, Modul neuesmodul,
                                            Modul changes, Field field) {
        field.setAccessible(true);

        if ("datumAenderung".equals(field.getName())) {
            return false;
        }

        try {
            if (field.get(altesmodul) == null) {
                if (field.get(neuesmodul) != null) {
                    field.set(changes, field.get(neuesmodul));
                    return true;
                }
            } else if (!field.get(altesmodul).equals(field.get(neuesmodul))) {
                field.set(changes, field.get(neuesmodul));
                return true;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }


    static void applyAntragOnModul(Modul modul, Antrag antrag) {
        Modul modulChanges = JsonService.jsonObjectToModul(antrag.getJsonModulAenderung());

        for (Field field : modul.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                if (field.get(modulChanges) == null) {
                    continue;
                }
                if (!field.get(modulChanges).equals(field.get(modul))) {
                    field.set(modul, field.get(modulChanges));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static Modul readModulFromWrapper(ModulWrapper modulWrapper) {
//        //TODO: Magic Numbers besser lösen
//        int anzahlZusatzfelderImWrapper;
//        if (modulWrapper.getZusatzfelder() == null) {
//            anzahlZusatzfelderImWrapper = 0;
//        } else {
//            anzahlZusatzfelderImWrapper = modulWrapper.getZusatzfelder().length;
//        }
//        int anzahlVeranstaltungsformenImWrapper;
//        if (modulWrapper.getVeranstaltungsformen() == null) {
//            anzahlVeranstaltungsformenImWrapper = 0;
//        } else {
//            anzahlVeranstaltungsformenImWrapper = modulWrapper.getVeranstaltungsformen().length;
//        }
//
//        int anzahlVeranstaltungen = modulWrapper.getVeranstaltungen().size();
//        int veranstaltungsformenProVeranstaltung = 6;
//        int zusatzfelderProVeranstaltung = 2;
//
//        if (anzahlVeranstaltungsformenImWrapper != veranstaltungsformenProVeranstaltung
//                * anzahlVeranstaltungen) {
//            throw new IllegalArgumentException(
//                    "falsche Größe der Veranstaltungsformenliste im Wrapper");
//        }
//
//        if (anzahlZusatzfelderImWrapper != zusatzfelderProVeranstaltung
//                        * anzahlVeranstaltungen) {
//            throw new IllegalArgumentException("falsche Größe der Zusatzfelderliste im Wrapper");
//        }

        //Auspacken des Wrappers
        for (int i = 0; i < modulWrapper.getVeranstaltungen().size(); i++) {
            modulWrapper.getVeranstaltungen().get(i).setVeranstaltungsformen(
                    new HashSet<>(modulWrapper.getVeranstaltungsformen()[i]));
            modulWrapper.getVeranstaltungen().get(i).setZusatzfelder(
                    new HashSet<>(modulWrapper.getZusatzfelder()[i]));
        }
        Set<Veranstaltung> veranstaltungenSet = new HashSet<>(modulWrapper.getVeranstaltungen());
//
//        for (int i = 0; i < anzahlVeranstaltungen; i++) {
//            for (int j = 0; j < veranstaltungsformenProVeranstaltung; j++) {
//                modulWrapper.getVeranstaltungen().get(i).getVeranstaltungsformen().add(
//                        modulWrapper.getVeranstaltungsformen().get(
//                                i * veranstaltungsformenProVeranstaltung + j));
//            }
//            for (int j = 0; j < zusatzfelderProVeranstaltung; j++) {
//                modulWrapper.getVeranstaltungen().get(i).getZusatzfelder().add(
//                        modulWrapper.getZusatzfelder().get(i * zusatzfelderProVeranstaltung + j));
//            }
//        }

//        //TODO: fixen, entfernt teilweise zu viele oder zu wenige wenn einige leer?
//        modulWrapper.getVeranstaltungsformen().removeIf(vf -> vf.getForm().isEmpty());
////
//        modulWrapper.getZusatzfelder().removeIf(z -> z.getTitel().isEmpty()
//                || z.getInhalt().isEmpty());

//        Set<Veranstaltung> veranstaltungen = new HashSet<>(modulWrapper.getVeranstaltungen());
        Modul modul = modulWrapper.getModul();
//        modul.getVeranstaltungen().clear();     //TODO: verwaiste Veranstaltungen entfernen!
        modul.setVeranstaltungen(veranstaltungenSet);
        modul.refreshMapping();
        return modul;
    }

    public List<Modul> getAllModule() {
        return StreamSupport.stream(modulSnapshotRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<Modul> getAllSichtbareModule() {
        return getAllModule().stream().filter(Modul::getSichtbar).collect(Collectors.toList());
    }

    public List<Modul> getModuleBySemester(String semester) {
        return modulSnapshotRepository.findModuleBySemester(semester);
    }

    public Modul getModulById(Long id) {
        return modulSnapshotRepository.findById(id).orElse(null);
    }

}
