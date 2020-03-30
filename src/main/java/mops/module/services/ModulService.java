package mops.module.services;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
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
     * @return Ein Änderungsmodul, bei dem ein Feld ohne Änderung den Wert null hat und
     *         ein Feld mit einem Wert den neuen Wert nach der Änderung enthält.
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

    /**Diese Methode wendet Änderungen in Form eines Antrages auf ein Modul an.
     *
     * @param modul auf das die Änderungen aus dem Antrag angewendet werden sollen.
     * @param antrag der die Änderungen beinhaltet.
     */

    public static void applyAntragOnModul(Modul modul, Antrag antrag) {
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

    public List<Modul> getAllModule() {
        return StreamSupport.stream(modulSnapshotRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Gibt alle sichtbaren Module, die ungleich null sind, zurück.
     *
     * @return alle sichtbaren Module, die ungleich null sind
     */
    public List<Modul> getAllSichtbareModule() {
        return getAllModule()
                .stream()
                .filter(m -> m.getSichtbar() != null)
                .filter(Modul::getSichtbar)
                .collect(Collectors.toList());
    }

    public List<Modul> getModuleBySemester(String semester) {
        return modulSnapshotRepository.findModuleBySemester(semester);
    }

    public Modul getModulById(Long id) {
        return modulSnapshotRepository.findById(id).orElse(null);
    }

    /**
     * Fügt einer Veranstaltung ein SemesterTag hinzu.
     *
     * @param semesterTag     Der SemesterTag, der der Veranstaltung hinzugefügt werden soll
     * @param veranstaltungId ID der Veranstaltung, die das Tag erhalten soll
     * @param modulId         ID des Moduls, das die Veranstaltung beinhaltet
     */
    public void tagVeranstaltungSemester(String semesterTag, Long veranstaltungId, Long modulId) {

        Veranstaltung veranstaltung = getModulById(modulId)
                .getVeranstaltungen()
                .stream()
                .filter(v -> v.getId().equals(veranstaltungId)).findFirst().orElse(null);
        if (veranstaltung == null) {
            throw new IllegalArgumentException("Ungültige ID der Veranstaltung!");
        }
        Set<String> semesterOld = veranstaltung.getSemester();
        semesterOld.add(semesterTag);

        modulSnapshotRepository.save(getModulById(modulId));
    }

    /**
     * Löscht ein gewünschtes SemesterTag einer Veranstaltung.
     *
     * @param semesterTag     Der SemesterTag, der gelöscht werden soll
     * @param veranstaltungId ID der Veranstaltung, die das Tag beinhaltet
     * @param modulId         ID des Moduls, das die Veranstaltung beinhaltet
     */
    public void deleteTagVeranstaltungSemester(
            String semesterTag,
            Long veranstaltungId,
            Long modulId) {

        Veranstaltung veranstaltung = getModulById(modulId)
                .getVeranstaltungen()
                .stream()
                .filter(v -> v.getId().equals(veranstaltungId)).findFirst().orElse(null);
        if (veranstaltung == null) {
            throw new IllegalArgumentException("Ungültige ID der Veranstaltung!");
        }

        Set<String> semesterNew = veranstaltung
                .getSemester()
                .stream()
                .filter(s -> !s.equals(semesterTag))
                .collect(Collectors.toSet());

        veranstaltung.setSemester(semesterNew);

        modulSnapshotRepository.save(getModulById(modulId));
    }

    /**
     * Gibt die letzten x und die y darauffolgenden Semester als Liste von Strings zurück.
     *
     * @param pastCount Anzahl der vergangenen Semester
     * @param nextCount Anzahl der darauffolgenden Semester (inklusive dem aktuellen Semester)
     * @return Liste von formatierten Semester-Strings
     */
    public static List<String> getPastAndNextSemesters(LocalDateTime when,
                                                       int pastCount, int nextCount) {
        List<String> semesterListe = new ArrayList<>();

        for (int i = pastCount * (-1); i < nextCount; i++) {
            semesterListe.add(getSemesterFromDate(when.plusMonths(6 * i)));
        }
        return semesterListe;
    }

    /**
     * Gibt das zugehörige Semester zum Datum zurück.
     *
     * @param when Datum
     * @return Formatierter Semester-String
     */
    public static String getSemesterFromDate(LocalDateTime when) {
        return getSemesterFromDate(when, false);
    }

    /**
     * Gibt das zugehörige Semester mit entsprechender Formatierung zum Datum zurück.
     *
     * @param when         Datum
     * @param formalFormat bool, ob Semester knapp (z.B. WiSe2019-20)
     *                     oder formal (z.B. Winter 2019) ausgegeben werden soll
     * @return Formatierter Semester-String
     */
    public static String getSemesterFromDate(LocalDateTime when, boolean formalFormat) {
        int currentYear = when.getYear();
        LocalDateTime ssStart = LocalDateTime.of(currentYear, 4, 1, 0, 0);
        LocalDateTime wsStart = LocalDateTime.of(currentYear, 10, 1, 0, 0);

        if (when.isBefore(ssStart)) {
            if (formalFormat) {
                return "Winter " + (currentYear - 1);
            } else {
                return "WiSe" + getWinterSemesterYear(currentYear - 1);
            }
        } else if (when.isAfter(wsStart)) {
            if (formalFormat) {
                return "Winter " + currentYear;
            } else {
                return "WiSe" + getWinterSemesterYear(currentYear);
            }
        } else {
            if (formalFormat) {
                return "Sommer " + currentYear;
            } else {
                return "SoSe" + currentYear;
            }
        }
    }

    /**
     * Formatiert den jeweiligen Wintersemester-String.
     *
     * @param firstYear Jahr, in dem das Wintersemester beginnt
     * @return Formatierter Wintersemester-String
     */
    public static String getWinterSemesterYear(int firstYear) {
        String secondYear = Integer.toString(firstYear + 1);
        secondYear = secondYear.length() > 2
                ? secondYear.substring(secondYear.length() - 2) : secondYear;
        return firstYear + "-" + secondYear;
    }

    /**Kopiert ein Modul.
     *
     * @param oldModul Das zu kopierende Modul.
     * @param newModul Die Kopie.
     */

    public static void copyModul(Modul oldModul, Modul newModul) {

        for (Field field : oldModul.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                field.set(newModul, field.get(oldModul));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Ändert die Sichtbarkeit eines Moduls.
     *
     * @param modulId Id des Moduls, dessen Sichtbarkeit sich ändern soll
     */
    public void changeVisibility(long modulId) {

        Boolean status = getModulById(modulId).getSichtbar();

        if (status == null || !status) {
            getModulById(modulId).setSichtbar(true);
        } else {
            getModulById(modulId).setSichtbar(false);
        }
        modulSnapshotRepository.save(getModulById(modulId));
    }

}
