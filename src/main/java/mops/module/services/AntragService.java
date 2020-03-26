package mops.module.services;

import java.time.LocalDateTime;
import java.util.Comparator;
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
public class AntragService {

    private final AntragRepository antragRepository;
    private final ModulSnapshotRepository modulSnapshotRepository;

    /**
     * Erstellt einen Änderungsantrag aus einem Modul heraus.
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
    public Antrag addModulModificationAntrag(Modul newModul, String antragsteller)
            throws IllegalArgumentException {
        Modul oldModul = modulSnapshotRepository.findById(newModul.getId()).orElse(null);
        if (oldModul == null) {
            throw new IllegalArgumentException("Fehlerhaftes Modul!");
        } else {
            Modul diffModul = ModulService.calculateModulDiffs(oldModul, newModul);

            if (diffModul == null) {
                throw new IllegalArgumentException("Das Modul wurde nicht verändert!");
            }

            diffModul.setDatumAenderung(LocalDateTime.now());
            Antrag antrag = modulToAntrag(diffModul);
            antrag.setDatumErstellung(LocalDateTime.now());
            antrag.setAntragsteller(antragsteller);
            antragRepository.save(antrag);
            return antrag;
        }
    }

    /**
     * Antrag für die Erstellung eines neuen Moduls abschicken.
     *
     * @param newModul Neues Modul
     */
    public Antrag addModulCreationAntrag(Modul newModul, String antragsteller) {
        newModul.setId(null);
        newModul.setDatumErstellung(LocalDateTime.now());
        newModul.setDatumAenderung(LocalDateTime.now());

        Antrag antrag = modulToAntrag(newModul);
        antrag.setDatumErstellung(LocalDateTime.now());
        antrag.setAntragsteller(antragsteller);
        antragRepository.save(antrag);
        return antrag;
    }

    /**
     * Genehmigt den Änderungsantrag.
     *
     * @param antrag Muss ein Modul beinhalten, das schon existiert
     */
    public Modul approveModulModificationAntrag(Antrag antrag) {
        Modul oldModul = modulSnapshotRepository.findById(antrag.getModulId()).orElse(null);
        if (oldModul == null) {
            throw new IllegalArgumentException(
                    "Modul konnte in der Datenbank nicht gefunden werden!");
        }
        ModulService.applyAntragOnModul(oldModul, antrag);
        oldModul.refreshMapping();
        Modul modul = modulSnapshotRepository.save(oldModul);

        antrag.setDatumGenehmigung(LocalDateTime.now());
        antragRepository.save(antrag);
        return modul;
    }

    /**
     * Fügt einen Antrag zur Erstellung eines neuen Moduls hinzu.
     * @param antrag Antrag mit neuem Modul, in dem die Modul id null ist
     */
    public Modul approveModulCreationAntrag(Antrag antrag) {
        Modul neuesmodul = JsonService.jsonObjectToModul(antrag.getJsonModulAenderung());
        neuesmodul.refreshMapping();

        Modul modul = modulSnapshotRepository.save(neuesmodul);

        antrag.setDatumGenehmigung(LocalDateTime.now());
        antrag.setModulId(modul.getId());
        antragRepository.save(antrag);
        return modul;
    }

    public List<Antrag> getAlleAntraege() {
        return StreamSupport.stream(antragRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<Antrag> getAlleAntraegeGeordnetDatum() {
        return getAlleAntraege().stream().sorted(Comparator.comparing(Antrag::getDatumErstellung))
                .collect(Collectors.toList());
    }

    public List<Antrag> getAlleOffenenAntraegeGeordnetDatum() {
        return getAlleAntraegeGeordnetDatum().stream().filter(x -> x.getDatumGenehmigung() == null)
                .collect(Collectors.toList());
    }

    public Antrag getAntragById(Long id) {
        return antragRepository.findById(id).orElse(null);
    }
}