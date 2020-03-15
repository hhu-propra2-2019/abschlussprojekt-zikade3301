package mops.module.services;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
     * Antrag für eine Moduländerung abschicken.
     *
     * @param newModul Neues Modul
     */
    public void addModulModificationAntrag(Modul newModul) {
        Optional<Modul> optionalModul = modulSnapshotRepository.findById(newModul.getId());
        if (optionalModul.isPresent()) {
            Modul diffModul = calculateModulDiffs(optionalModul.get(), newModul);
            Antrag antrag = toAntrag(diffModul);
            antragsRepository.save(antrag);
        } else {
            throw new IllegalArgumentException("Fehlerhaftes Modul!");
        }

    }

    /**
     * Antrag für die Erstellung eines neuen Moduls abschicken.
     *
     * @param newModul Neues Modul
     */
    public void addModulCreationAntrag(Modul newModul) {
        newModul.setId(null);

        Antrag antrag = toAntrag(newModul);
        antragsRepository.save(antrag);
    }

    public void approveModulModificationAntrag(Antrag antrag) {
        Modul altesmodul = modulSnapshotRepository.findById(antrag.getModulid()).orElse(null);
        if (altesmodul == null) {
            throw new IllegalArgumentException(
                    "Modul konnte in der Datenbank nicht gefunden werden!");
        }
        applyAntragOnModul(altesmodul, antrag);
        altesmodul.refreshLinks();
        modulSnapshotRepository.deleteById(altesmodul.getId());
        modulSnapshotRepository.save(altesmodul);

        antrag.setApproveDate(LocalDateTime.now());
        antragsRepository.save(antrag);
    }

    public void approveModulCreationAntrag(Antrag antrag) {
        Modul neuesmodul = jsonService.jsonObjectToModul(antrag.getModul());

        neuesmodul.refreshLinks();

        modulSnapshotRepository.save(neuesmodul);

        antrag.setApproveDate(LocalDateTime.now());
        antrag.setModulid(neuesmodul.getId());
        antragsRepository.save(antrag);
    }

    /**
     * Erstellt aus einem Modul ein Antrag.
     *
     * @param modul Modul auf welches der Antrag angewendet wird
     * @return
     */
    Antrag toAntrag(Modul modul) {
        String jsonObject = jsonService.modulToJsonObject(modul);
        Antrag antrag = new Antrag();
        antrag.setModul(jsonObject);
        antrag.setModulid(modul.getId());
        //antrag.setApproveDate(approveDate);
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

    List<Modul> getAlleModule() {
        return StreamSupport.stream(modulSnapshotRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<Modul> getAlleSichtbarenModule() {
        return getAlleModule().stream().filter(Modul::getSichtbar).collect(Collectors.toList());
    }

    List<Antrag> getAlleAntraege() {
        return StreamSupport.stream(antragsRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    /*Modul getModulUntil(Long modulid, LocalDateTime when) {
        List<Antrag> antraege = getAntraegeForModulUntil(modulid, when);
        return buildModulFromAntraege(antraege);
    }

    List<Antrag> getAntraegeForModulUntil(Long modulid, LocalDateTime when) {
        List<Antrag> antraege = antragsRepository.findByModulid(modulid);
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
}
