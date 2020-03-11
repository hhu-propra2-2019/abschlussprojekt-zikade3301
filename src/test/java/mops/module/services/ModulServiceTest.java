package mops.module.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import mops.module.repositories.AntragsRepository;
import mops.module.repositories.ModulSnapshotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ModulServiceTest {
    private ModulService modulService;
    private JSONService jsonService;
    private AntragsRepository antragsRepository;
    private ModulSnapshotRepository modulSnapshotRepository;


    @BeforeEach
    public void init() {
        antragsRepository = mock(AntragsRepository.class);
        modulSnapshotRepository = mock(ModulSnapshotRepository.class);
        jsonService = new JSONService();
        modulService = new ModulService(antragsRepository, modulSnapshotRepository, jsonService);
    }

    @Test
    public void calculateModulDiffsTest1() {
        Modul modul1 = new Modul();
        modul1.setModulkategorie(Modulkategorie.MASTERARBEIT);
        Veranstaltung veranstaltung1 = new Veranstaltung();
        veranstaltung1.setId((long) 3);
        List<Veranstaltung> veranstaltungList1 = new ArrayList<Veranstaltung>();
        veranstaltungList1.add(veranstaltung1);
        modul1.setVeranstaltungen(veranstaltungList1);
        modul1.setId((long) 5);

        Modul modul2 = new Modul();
        modul2.setModulkategorie(Modulkategorie.BACHELORARBEIT);
        Veranstaltung veranstaltung2 = new Veranstaltung();
        veranstaltung2.setId((long) 3);
        List<Veranstaltung> veranstaltungList2 = new ArrayList<Veranstaltung>();
        veranstaltungList2.add(veranstaltung2);
        modul2.setVeranstaltungen(veranstaltungList2);
        modul2.setId((long) 5);

        Modul diffs = modulService.calculateModulDiffs(modul1, modul2);
        assertThat(diffs.getModulkategorie()).isEqualTo(Modulkategorie.BACHELORARBEIT);
        assertThat(diffs.getVeranstaltungen()).isEqualTo(null);
    }

    @Test
    public void calculateModulDiffsTest2() {
        Modul modul1 = new Modul();
        modul1.setModulkategorie(Modulkategorie.MASTERARBEIT);

        Veranstaltung veranstaltung1 = new Veranstaltung();
        veranstaltung1.setId((long) 3);
        Veranstaltung veranstaltung3 = new Veranstaltung();
        List<Veranstaltung> veranstaltungsVoraussetzungen = new ArrayList<Veranstaltung>();
        veranstaltungsVoraussetzungen.add(veranstaltung3);
        veranstaltung1.setVoraussetzungenTeilnahme(veranstaltungsVoraussetzungen);
        List<Veranstaltung> veranstaltungList1 = new ArrayList<Veranstaltung>();
        veranstaltungList1.add(veranstaltung1);
        modul1.setVeranstaltungen(veranstaltungList1);

        modul1.setId((long) 5);

        Modul modul2 = new Modul();
        modul2.setModulkategorie(Modulkategorie.BACHELORARBEIT);

        Veranstaltung veranstaltung2 = new Veranstaltung();
        veranstaltung2.setId((long) 3);
        Veranstaltung veranstaltung4 = new Veranstaltung();
        List<Veranstaltung> veranstaltungsVoraussetzungen2 = new ArrayList<Veranstaltung>();
        veranstaltungsVoraussetzungen2.add(veranstaltung4);
        veranstaltung2.setVoraussetzungenTeilnahme(veranstaltungsVoraussetzungen2);
        List<Veranstaltung> veranstaltungList2 = new ArrayList<Veranstaltung>();
        veranstaltungList2.add(veranstaltung2);
        modul2.setVeranstaltungen(veranstaltungList2);

        modul2.setId((long) 5);

        Modul diffs = modulService.calculateModulDiffs(modul1, modul2);
        assertThat(diffs.getModulkategorie()).isEqualTo(Modulkategorie.BACHELORARBEIT);
        assertThat(diffs.getVeranstaltungen()).isEqualTo(null);
    }

    @Test
    public void addModulTestNewModul() {
        Modul newModul = new Modul();
        newModul.setId(1L);
        newModul.setModulkategorie(Modulkategorie.MASTERARBEIT);
        Veranstaltung veranstaltung1 = new Veranstaltung();
        Veranstaltung veranstaltung2 = new Veranstaltung();
        veranstaltung1.setId(1L);
        veranstaltung2.setId(2L);
        List<Veranstaltung> veranstaltungen1 = new ArrayList<>();
        veranstaltungen1.add(veranstaltung2);
        veranstaltung1.setVoraussetzungenTeilnahme(veranstaltungen1);
        newModul.setVeranstaltungen(veranstaltungen1);
        LocalDateTime approveDate = LocalDateTime.now();

        Optional<Modul> empty = Optional.empty();
        when(modulSnapshotRepository.findById(1L)).thenReturn(empty);
        modulService.addModul(newModul, approveDate);

        Antrag newAntrag = modulService.toAntrag(newModul, approveDate);

        System.out.println("*****************");
        System.out.println(antragsRepository.count());
        antragsRepository.save(newAntrag);
        System.out.println(antragsRepository.count());

    }

    @Disabled
    @Test
    public void addModulTestoverwrite() {
        Modul oldModul = new Modul();
        oldModul.setId(1L);
        oldModul.setModulkategorie(Modulkategorie.MASTERARBEIT);
        Veranstaltung veranstaltung1 = new Veranstaltung();
        veranstaltung1.setId(1L);
        List<Veranstaltung> veranstaltungen1 = new ArrayList<Veranstaltung>();
        veranstaltungen1.add(veranstaltung1);
        veranstaltung1.setVoraussetzungenTeilnahme(veranstaltungen1);
        oldModul.setVeranstaltungen(veranstaltungen1);

        Modul newModul = new Modul();
        newModul.setId(1L);
        newModul.setModulkategorie(Modulkategorie.BACHELORARBEIT);
        Veranstaltung veranstaltung = new Veranstaltung();
        veranstaltung.setId(1L);
        List<Veranstaltung> veranstaltungen = new ArrayList<Veranstaltung>();
        veranstaltungen.add(veranstaltung);
        veranstaltung.setVoraussetzungenTeilnahme(veranstaltungen);
        newModul.setVeranstaltungen(veranstaltungen);

        //when(modulSnapshotRepository.findById(newModul.getId())).thenReturn(newModul);
    }
}
