package mops.module.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;
import mops.module.repositories.AntragsRepository;
import mops.module.repositories.ModulSnapshotRepository;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
//@ActiveProfiles("dev")
public class ModulServiceDatabaseTest {
    private ModulService modulService;
    private JsonService jsonService;

    @Autowired
    private AntragsRepository antragsRepository;

    @Autowired
    private ModulSnapshotRepository modulSnapshotRepository;
    private String modul1, modul2, modul3, modul4, diffs1, diffs2;

    @AfterEach
    public void clear() {
        //antragsRepository.deleteAll();
        //modulSnapshotRepository.deleteAll();
    }

    @BeforeEach
    public void init() {
        //antragsRepository = mock(AntragsRepository.class);
        //modulSnapshotRepository = mock(ModulSnapshotRepository.class);
        jsonService = new JsonService();
        modulService = new ModulService(antragsRepository, modulSnapshotRepository, jsonService);

        modul1 = "{\"veranstaltungen\":[{\"creditPoints\":\"5CP\"}]," +
                "\"modulkategorie\":\"MASTERARBEIT\"}";
        modul2 = "{\"veranstaltungen\":[{\"creditPoints\":\"5CP\"}]," +
                "\"modulkategorie\":\"BACHELORARBEIT\"}";
        modul3 = "{\"veranstaltungen\":[{\"creditPoints\":\"5CP\"," +
                "\"beschreibung\":{\"inhalte\":\"Lorem ipsum\"}}]," +
                "\"modulkategorie\":\"MASTERARBEIT\"}";
        modul4 = "{\"veranstaltungen\":[{\"creditPoints\":\"5CP\"," +
                "\"beschreibung\":{\"inhalte\":\"Lorem ipsum\"}}]," +
                "\"modulkategorie\":\"BACHELORARBEIT\"}";
        diffs1 = "{\"modulkategorie\":\"BACHELORARBEIT\"}";
        diffs2 = "{\"veranstaltungen\":[{\"id\":3," +
                "\"voraussetzungenTeilnahme\":[{\"titel\":\"test\"}]}]," +
                "\"modulkategorie\":\"BACHELORARBEIT\"}";
    }

    @Test
    public void addModulCreationAntragTestCreateOne() {
        modulService.addModulCreationAntrag(jsonService.jsonObjectToModul(modul1));
        assertThat(antragsRepository.count()).isEqualTo(1);
    }

    @Test
    public void addModulModificationAntragTestCreateOne() {
        modulService.addModulModificationAntrag(jsonService.jsonObjectToModul(modul1));
        assertThat(antragsRepository.count()).isEqualTo(1);
    }

    @Test
    public void approveModulCreationAntragTestAntragCreated() {
        modulService.addModulCreationAntrag(jsonService.jsonObjectToModul(modul1));
        Antrag antrag = modulService.getAlleAntraege().get(0);
        modulService.approveModulCreationAntrag(antrag);
        assertThat(modulSnapshotRepository.count()).isEqualTo(1);
    }

    @Test
    public void approveModulCreationAntragTestModulIdIsAddedToAntrag() {
        modulService.addModulCreationAntrag(jsonService.jsonObjectToModul(modul1));
        Antrag antrag = modulService.getAlleAntraege().get(0);
        modulService.approveModulCreationAntrag(antrag);
        Modul modul = modulService.getAlleModule().get(0);
        assertThat(antrag.getModulid()).isEqualTo(modul.getId());
    }

    @Test
    public void jpatest() {
        Modul vergleichsmodul = new Modul();
        vergleichsmodul.setModulkategorie(Modulkategorie.MASTERARBEIT);
        Veranstaltung veranstaltung = new Veranstaltung();
        veranstaltung.setCreditPoints("5CP");
        Veranstaltungsbeschreibung veranstaltungsbeschreibung = new Veranstaltungsbeschreibung();
        veranstaltungsbeschreibung.setInhalte("Hier sind Inhalte");
        veranstaltung.setBeschreibung(veranstaltungsbeschreibung);
        vergleichsmodul.addVeranstaltung(veranstaltung);

        modulService.addModulCreationAntrag(vergleichsmodul);
        List<Antrag> antraege = modulService.getAlleAntraege();
        modulService.approveModulCreationAntrag(antraege.get(antraege.size() - 1));

        List<Modul> module = modulService.getAlleModule();
        Modul modul = module.get(module.size() - 1);
        vergleichsmodul.setId(modul.getId());

        try {
            JSONAssert.assertEquals(jsonService.modulToJsonObject(modul),
                    jsonService.modulToJsonObject(vergleichsmodul), false);
        } catch (JSONException e) {
            fail(e.toString());
        }
    }

    @Test
    public void approveModulModificationAntragTest() {
        modulService.addModulCreationAntrag(jsonService.jsonObjectToModul(modul3));

        List<Antrag> antraege = modulService.getAlleAntraege();
        modulService.approveModulCreationAntrag(antraege.get(antraege.size() - 1));

        List<Modul> module = modulService.getAlleModule();
        Modul modul = module.get(module.size() - 1);

        Modul aenderungen = jsonService.jsonObjectToModul(modul4);

        aenderungen.setId(modul.getId());
        modulService.addModulModificationAntrag(aenderungen);

        antraege = modulService.getAlleAntraege();
        Antrag antrag = antraege.get(antraege.size() - 1);

        modulService.approveModulModificationAntrag(antrag);

        module = modulService.getAlleModule();
        Modul geaendertesModul = module.get(module.size() - 1);

        Modul assertmodul = jsonService.jsonObjectToModul(modul4);
        assertmodul.setId(geaendertesModul.getId());

        try {
            JSONAssert.assertEquals(jsonService.modulToJsonObject(geaendertesModul),
                    jsonService.modulToJsonObject(assertmodul), false);
        } catch (JSONException e) {
            fail(e.toString());
        }

    }

}
