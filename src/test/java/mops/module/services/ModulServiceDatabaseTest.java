package mops.module.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;
import mops.module.repositories.AntragRepository;
import mops.module.repositories.ModulSnapshotRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class ModulServiceDatabaseTest {
    private ModulService modulService;
    private AntragService antragService;

    @Autowired
    private AntragRepository antragRepository;

    @Autowired
    private ModulSnapshotRepository modulSnapshotRepository;

    private String modul1;
    private String modul2;
    private String modul3;
    private String modul4;
    private String diffs1;
    private String diffs2;
    private String completeModul;

    private CustomComparator ignoreDates;

    /**
     * Erstellt Beispiel-Objekte als Strings, aus denen anschließend JsonService Module erstellt.
     */
    @BeforeEach
    public void init() {
        modulService = new ModulService(antragRepository, modulSnapshotRepository);
        antragService = new AntragService(antragRepository, modulSnapshotRepository);

        // Ignore dates for JSON comparison
        ignoreDates = new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("datumErstellung", (o1, o2) -> true),
                new Customization("datumAenderung", (o1, o2) -> true));

        antragRepository.deleteAll();
        modulSnapshotRepository.deleteAll();

        modul1 = "{'veranstaltungen':[{'creditPoints':'5CP'}],"
                + "'modulkategorie':'MASTERARBEIT'}";
        modul2 = "{'veranstaltungen':[{'creditPoints':'5CP'}],"
                + "'modulkategorie':'BACHELORARBEIT'}";
        modul3 = "{'veranstaltungen':[{'creditPoints':'5CP',"
                + "'beschreibung':{'inhalte':'Lorem ipsum'}}],"
                + "'modulkategorie':'MASTERARBEIT'}";
        modul4 = "{'veranstaltungen':[{'creditPoints':'5CP',"
                + "'beschreibung':{'inhalte':'Lorem ipsum'}}],"
                + "'modulkategorie':'BACHELORARBEIT'}";
        diffs1 = "{'modulkategorie':'BACHELORARBEIT'}";
        diffs2 = "{'veranstaltungen':[{'id':3,"
                + "'voraussetzungenTeilnahme':[{'titel':'test'}]}],"
                + "'modulkategorie':'BACHELORARBEIT'}";
        completeModul = "{'titelDeutsch':'Betriebssysteme','titelEnglisch':'Operating systems',"
                + "'veranstaltungen':[{'titel':'Vorlesung Betriebssysteme',"
                + "'leistungspunkte':'10CP','veranstaltungsformen':['Vorlesung',"
                + "'Praktische Übung'],'beschreibung':{'inhalte':'Inhalte',"
                + "'lernergebnisse':'Synchronisierung','literatur':['Alter Schinken'],"
                + "'verwendbarkeit':['Überall verwendbar'],"
                + "'voraussetzungenBestehen':['50% der Punkte in der Klausur'],"
                + "'haeufigkeit':'Alle 2 Semester','sprache':'Deutsch'},"
                + "'voraussetzungenTeilnahme':['Informatik I']}],"
                + "'modulbeauftragte':['Michael Schöttner'],'gesamtLeistungspunkte':'10CP',"
                + "'studiengang':'Informatik','modulkategorie':'WAHLPFLICHT_BA',"
                + "'zusatzfelder':[{'titel':'Feld2','inhalt':'Numero dos'},{'titel':'Feld1',"
                + "'inhalt':'Dies hier ist das erste Zusatzfeld!'}]}";
    }

    @Test
    public void addModulCreationAntragTestCreateOne() {
        antragService.addModulCreationAntrag(JsonService.jsonObjectToModul(modul1));
        assertThat(antragRepository.count()).isEqualTo(1);
    }

    @Test
    public void approveModulCreationAntragTestAntragCreated() {
        antragService.addModulCreationAntrag(JsonService.jsonObjectToModul(modul1));
        Antrag antrag = antragService.getAlleAntraege().get(0);
        antragService.approveModulCreationAntrag(antrag);
        assertThat(modulSnapshotRepository.count()).isEqualTo(1);
    }

    @Test
    public void approveModulCreationAntragTestModulIdIsAddedToAntrag() {
        antragService.addModulCreationAntrag(JsonService.jsonObjectToModul(modul1));
        Antrag antrag = antragService.getAlleAntraege().get(0);
        antragService.approveModulCreationAntrag(antrag);
        Modul modul = modulService.getAllModule().get(0);
        assertThat(antrag.getModulId()).isEqualTo(modul.getId());
    }

    @Test
    public void approveModulCreationAntragTestFullRoutine() {
        Modul vergleichsmodul = new Modul();
        vergleichsmodul.setModulkategorie(Modulkategorie.MASTERARBEIT);
        Veranstaltung veranstaltung = new Veranstaltung();
        veranstaltung.setLeistungspunkte("5CP");
        Veranstaltungsbeschreibung veranstaltungsbeschreibung = new Veranstaltungsbeschreibung();
        veranstaltungsbeschreibung.setInhalte("Hier sind Inhalte");
        veranstaltung.setBeschreibung(veranstaltungsbeschreibung);
        vergleichsmodul.addVeranstaltung(veranstaltung);

        antragService.addModulCreationAntrag(vergleichsmodul);
        List<Antrag> antraege = antragService.getAlleAntraege();
        antragService.approveModulCreationAntrag(antraege.get(antraege.size() - 1));

        List<Modul> module = modulService.getAllModule();
        Modul modul = module.get(module.size() - 1);
        vergleichsmodul.setId(modul.getId());

        try {
            JSONAssert.assertEquals(JsonService.modulToJsonObject(vergleichsmodul),
                    JsonService.modulToJsonObject(modul), ignoreDates);
        } catch (JSONException e) {
            fail(e.toString());
        }
    }

    @Test
    public void approveModulModificationAntragTest() {
        antragService.addModulCreationAntrag(JsonService.jsonObjectToModul(modul3));

        List<Antrag> antraege = antragService.getAlleAntraege();
        antragService.approveModulCreationAntrag(antraege.get(antraege.size() - 1));

        List<Modul> module = modulService.getAllModule();
        Modul modul = module.get(module.size() - 1);

        Modul aenderungen = JsonService.jsonObjectToModul(modul4);

        aenderungen.setId(modul.getId());
        antragService.addModulModificationAntrag(aenderungen);

        antraege = antragService.getAlleAntraege();
        Antrag antrag = antraege.get(antraege.size() - 1);

        antragService.approveModulModificationAntrag(antrag);

        module = modulService.getAllModule();
        Modul geaendertesModul = module.get(module.size() - 1);

        try {
            JSONAssert.assertEquals(JsonService.modulToJsonObject(aenderungen),
                    JsonService.modulToJsonObject(geaendertesModul), ignoreDates);
        } catch (JSONException e) {
            fail(e.toString());
        }

    }

    @Test
    public void completeAddModulRoutineTest() {

        Modul modul = JsonService.jsonObjectToModul(completeModul);
        Set<String> modulbeauftragte = new HashSet<>();
        modulbeauftragte.add("Michael Schöttner");
        modul.setModulbeauftragte(modulbeauftragte);
        System.out.println(JsonService.modulToJsonObject(modul));
        modul.refreshMapping();
        antragService.addModulCreationAntrag(modul);

        List<Antrag> antraege = antragService.getAlleAntraege();
        antragService.approveModulCreationAntrag(antraege.get(antraege.size() - 1));

        List<Modul> module = modulService.getAllModule();
        Modul dbmodul = module.get(module.size() - 1);

        dbmodul.setModulbeauftragte(new HashSet<>(Arrays.asList("Stefan Harmeling")));

        antragService.addModulModificationAntrag(dbmodul);

        antraege = antragService.getAlleAntraege();
        antragService.approveModulModificationAntrag(antraege.get(antraege.size() - 1));

        Optional<Modul> optionalModul = modulSnapshotRepository.findById(dbmodul.getId());

        if (optionalModul.isPresent()) {
            assertThat(dbmodul.getModulbeauftragte()).contains("Stefan Harmeling");
        }
    }

    @Test
    public void semesterQueryTest() {
        Modul modul1 = JsonService.jsonObjectToModul(completeModul);
        Modul modul2 = JsonService.jsonObjectToModul(completeModul);

        modul1.getVeranstaltungen().stream().findFirst().orElse(null)
                .setSemester(new HashSet<>(Arrays.asList("WiSe2018/19", "SoSe2019")));
        modul2.getVeranstaltungen().stream().findFirst().orElse(null)
                .setSemester(new HashSet<>(Arrays.asList("WiSe2019/20")));

        antragService.addModulCreationAntrag(modul1);
        antragService.addModulCreationAntrag(modul2);

        List<Antrag> antraege = antragService.getAlleAntraege();
        antraege.forEach(a -> antragService.approveModulCreationAntrag(a));

        List<Modul> dbmodule = modulSnapshotRepository.findModuleBySemester("WiSe2018/19");

        try {
            assertThat(dbmodule.size()).isEqualTo(1);
            JSONAssert.assertEquals(JsonService.modulToJsonObject(modul1),
                    JsonService.modulToJsonObject(dbmodule.get(0)), ignoreDates);
        } catch (JSONException e) {
            fail(e.toString());
        }
    }

}
