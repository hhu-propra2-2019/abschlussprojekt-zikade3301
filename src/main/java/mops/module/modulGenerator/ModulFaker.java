package mops.module.modulGenerator;

import com.github.javafaker.Faker;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;

public class ModulFaker {
    private static final Faker faker = new Faker();
    private static final Modulkategorie[] modulkategorrie = {Modulkategorie.PFLICHT_INFO, Modulkategorie.BACHELORARBEIT, Modulkategorie.NEBENFACH};
    private static final String semester[] = {"WS19", "SS19", "WS/SS20"};

    public static Modul generateFakeModul() {
        Modul fakeModul = new Modul();
        generateMultipleVeranstaltungen(fakeModul);
        fakeModul.setTitelDeutsch(faker.book().title());
        fakeModul.setGesamtLeistungspunkte("10CP");
        fakeModul.setStudiengang(faker.harryPotter().house());
        fakeModul.setModulkategorie(chooseRandom(modulkategorrie));
        fakeModul.setSichtbar(true);
        fakeModul.setModulbeauftragte(new HashSet<>(Arrays.asList(faker.harryPotter().character(), faker.harryPotter().character())));
        return fakeModul;
    }

    private static void generateMultipleVeranstaltungen(Modul fakeModul) {
        double randomNumber = Math.random() * 5;
        for (int i = 0; i < randomNumber; i++) {
            fakeModul.addVeranstaltung(generateFakeVeranstaltung());
        }
    }

    private static Veranstaltung generateFakeVeranstaltung() {
        Veranstaltung fakeVeranstaltung = new Veranstaltung();
        Veranstaltungsbeschreibung fakeBeshcreibung = generateFakeBeschreibung();
        fakeVeranstaltung.setTitel(faker.book().title());
        fakeVeranstaltung.setLeistungspunkte("5CP");
        fakeVeranstaltung.setBeschreibung(fakeBeshcreibung);
        fakeVeranstaltung.setLehrende(new HashSet<>(Arrays.asList(faker.harryPotter().character(), faker.harryPotter().character())));
        fakeVeranstaltung.setVeranstaltungsformen(new HashSet<>(Arrays.asList(faker.backToTheFuture().quote())));
        fakeVeranstaltung.setVoraussetzungenTeilnahme(new HashSet<>(Arrays.asList(faker.book().title(), faker.book().title())));
        fakeVeranstaltung.setSemester(chooseSetRandom(semester));
        return fakeVeranstaltung;
    }

    private static Veranstaltungsbeschreibung generateFakeBeschreibung() {
        Veranstaltungsbeschreibung fakeBeshcreibung = new Veranstaltungsbeschreibung();
        fakeBeshcreibung.setInhalte(faker.elderScrolls().quote());
        fakeBeshcreibung.setLernergebnisse(faker.elderScrolls().quote());
        fakeBeshcreibung.setHaeufigkeit("Einmal im leben");
        fakeBeshcreibung.setSprache(faker.country().name());
        fakeBeshcreibung.setLiteratur(new HashSet<>(Arrays.asList(faker.book().title(), faker.book().title())));
        fakeBeshcreibung.setVerwendbarkeit(new HashSet<>(Arrays.asList(faker.elderScrolls().quote())));
        fakeBeshcreibung.setVoraussetzungenBestehen(new HashSet<>(Arrays.asList(faker.elderScrolls().quote(), faker.elderScrolls().quote())));
        return fakeBeshcreibung;
    }

    private static <T extends Object> T chooseRandom(T[] objects) {
        return objects[(int) (Math.random() * objects.length)];
    }

    private static <T extends Object> Set<T> chooseSetRandom(T[] objects) {
        Set<T> set = new HashSet<>();
        double randomNumber = Math.random() * objects.length;
        for (int i = 0; i < randomNumber; i++) {
            set.add(objects[i]);
        }
        return set;
    }

}
