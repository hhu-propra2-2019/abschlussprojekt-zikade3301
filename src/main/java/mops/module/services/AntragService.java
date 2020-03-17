package mops.module.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
public class AntragService {

    private final AntragRepository antragRepository;
    private final ModulSnapshotRepository modulSnapshotRepository;

    /**
     * Erstellt einen Änderungsantrag aus einem Modul heraus
     *
     * @param modul Modul auf welches der Antrag angewendet wird
     * @return
     */
    Antrag modulToAntrag(Modul modul) {
        String jsonObject = JsonService.modulToJsonObject(modul);
        Antrag antrag = new Antrag();
        antrag.setJsonModulAenderung(jsonObject);
        antrag.setModulId(modul.getId());
        return antrag;

    }

    /**
     * Antrag für eine Moduländerung abschicken.
     * Falls das Modul nicht verändert wurde, oder ein nicht existierendes Modul übergeben wurde,
     * wird eine Exception geworfen.
     *
     * @param newModul Neues Modul mit korrekter ID!
     */
    public void addModulModificationAntrag(Modul newModul) throws IllegalArgumentException {
        Optional<Modul> optionalModul = modulSnapshotRepository.findById(newModul.getId());
        if (optionalModul.isPresent()) {
            Modul diffModul = ModulService.calculateModulDiffs(optionalModul.get(), newModul);

            if (diffModul == null) {
                throw new IllegalArgumentException("Das Modul wurde nicht verändert!");
            }

            diffModul.setDatumAenderung(LocalDateTime.now());
            Antrag antrag = modulToAntrag(diffModul);
            antrag.setDatumErstellung(LocalDateTime.now());
            antrag.setAntragsteller("Anonym");
            antragRepository.save(antrag);
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
        newModul.setDatumErstellung(LocalDateTime.now());
        newModul.setDatumAenderung(LocalDateTime.now());

        Antrag antrag = modulToAntrag(newModul);
        antrag.setDatumErstellung(LocalDateTime.now());
        antrag.setAntragsteller("Anonym");
        antragRepository.save(antrag);
    }

    /**
     * Genehmigt den Änderungsantrag.
     *
     * @param antrag Muss ein Modul beinhalten, das schon existiert
     */
    public void approveModulModificationAntrag(Antrag antrag) {
        Modul altesmodul = modulSnapshotRepository.findById(antrag.getModulId()).orElse(null);
        if (altesmodul == null) {
            throw new IllegalArgumentException(
                    "Modul konnte in der Datenbank nicht gefunden werden!");
        }
        ModulService.applyAntragOnModul(altesmodul, antrag);
        altesmodul.refreshLinks();
        modulSnapshotRepository.save(altesmodul);

        antrag.setDatumGenehmigung(LocalDateTime.now());
        antragRepository.save(antrag);
    }

    /**
     * Fügt einen Antrag zur Erstellung eines neuen Moduls hinzu.
     * @param antrag Antrag mit neuem Modul, indem die Modul id null ist
     */
    public void approveModulCreationAntrag(Antrag antrag) {
        Modul neuesmodul = JsonService.jsonObjectToModul(antrag.getJsonModulAenderung());

        neuesmodul.refreshLinks();

        modulSnapshotRepository.save(neuesmodul);

        antrag.setDatumGenehmigung(LocalDateTime.now());
        antrag.setModulId(neuesmodul.getId());
        antragRepository.save(antrag);
    }

    public List<Antrag> getAlleAntraege() {
        return StreamSupport.stream(antragRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

}
