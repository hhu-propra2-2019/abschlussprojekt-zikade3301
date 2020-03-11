package mops.module.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;


import java.util.ArrayList;
import java.util.List;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import mops.module.repositories.AntragsRepository;
import mops.module.repositories.ModulSnapshotRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ModulServiceTest {
    private static ModulService modulService;
    private static JSONService jsonService;

    @BeforeAll
    static void init() {
        AntragsRepository antragsRepository = mock(AntragsRepository.class);
        ModulSnapshotRepository modulSnapshotRepository = mock(ModulSnapshotRepository.class);
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
}
