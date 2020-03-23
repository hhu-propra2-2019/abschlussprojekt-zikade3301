package mops.module.modulGenerator;

import com.github.javafaker.Faker;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;
import mops.module.database.Veranstaltungsform;

public class ModulFaker {
    private static final Faker faker = new Faker();
    private static final String semester[] = {"WS19", "SS19", "WS/SS20"};

    public static Modul generateFakeModul() {
        Modul fakeModul = new Modul();
        generateMultipleVeranstaltungen(fakeModul);
        generateMultipleBeauftragte(fakeModul);
        fakeModul.setTitelDeutsch(faker.book().title());
        fakeModul.setTitelDeutsch("Kein Englisch");
        fakeModul.setGesamtLeistungspunkte("10CP");
        fakeModul.setStudiengang(faker.harryPotter().house());
        fakeModul.setModulkategorie(chooseRandom(Modulkategorie.values()));
        fakeModul.setSichtbar(true);
        return fakeModul;
    }

    private static void generateMultipleBeauftragte(Modul fakeModul) {
        double randomNumber = Math.random() * 5;
        Set<String> personen = new HashSet<>();
        for (int i = 0; i < randomNumber; i++) {
            personen.add(faker.harryPotter().character());
        }
        fakeModul.setModulbeauftragte(personen);
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
        generateMultipleVeranstaltungsform(fakeVeranstaltung);
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

    private static void generateMultipleVeranstaltungsform(Veranstaltung fakeVeranstaltung) {
        double randomNumber = Math.random() * 5;
        Set<Veranstaltungsform> fomen = new HashSet<>();
        for (int i = 0; i < randomNumber; i++) {
            fomen.add(generateFakeVeranstaltungsform());
        }
        fakeVeranstaltung.setVeranstaltungsformen(fomen);
    }

    private static Veranstaltungsform generateFakeVeranstaltungsform() {
        Veranstaltungsform veranstaltungsform = new Veranstaltungsform();
        veranstaltungsform.setForm("form");
        veranstaltungsform.setSemesterWochenStunden(faker.number().randomDigit());
        return veranstaltungsform;
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
