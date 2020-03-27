package mops.module.generator;

import com.github.javafaker.Faker;
import java.util.HashSet;
import java.util.Set;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;
import mops.module.database.Veranstaltungsform;

public class ModulFaker {
    private static final Faker faker = new Faker();
    private static final String[] semester = {"SoSe2019", "WiSe2019-20"};

    /**
     * Generiert fake Module für das Testing.
     *
     * @return
     */
    public static Modul generateFakeModul() {
        Modul fakeModul = new Modul();
        generateMultipleVeranstaltungen(fakeModul);
        generateMultipleBeauftragte(fakeModul);
        fakeModul.setTitelDeutsch(faker.book().title());
        fakeModul.setTitelEnglisch("Kein Englisch");
        fakeModul.setGesamtLeistungspunkte("10CP");
        fakeModul.setStudiengang(faker.harryPotter().house());
        fakeModul.setModulkategorie(chooseRandom(Modulkategorie.values()));
        fakeModul.setSichtbar(true);
        return fakeModul;
    }

    private static void generateMultipleBeauftragte(Modul fakeModul) {
        double randomNumber = Math.random() * 5;
        String personen = "";
        for (int i = 0; i < randomNumber; i++) {
            if (i != 0) {
                personen += (", ");
            }
            personen += (faker.harryPotter().character());
        }
        fakeModul.setModulbeauftragte(personen);
    }

    private static void generateMultipleVeranstaltungen(Modul fakeModul) {
        double randomNumber = Math.random() * 5;
        // MINDESTENS EINE VERANSTALTUNG IST NOTWENDIG!
        if (randomNumber == 0.0) {
            fakeModul.addVeranstaltung(generateFakeVeranstaltung());
        }
        for (int i = 0; i < randomNumber; i++) {
            fakeModul.addVeranstaltung(generateFakeVeranstaltung());
        }
    }

    private static Veranstaltung generateFakeVeranstaltung() {
        Veranstaltung fakeVeranstaltung = new Veranstaltung();
        Veranstaltungsbeschreibung fakeBeschreibung = generateFakeBeschreibung();
        fakeVeranstaltung.setTitel(faker.book().title());
        fakeVeranstaltung.setLeistungspunkte("5CP");
        fakeVeranstaltung.setBeschreibung(fakeBeschreibung);
        generateMultipleVeranstaltungsform(fakeVeranstaltung);
        fakeVeranstaltung.setVoraussetzungenTeilnahme(faker.elderScrolls().quote());
        fakeVeranstaltung.setSemester(chooseSetRandom(semester));
        return fakeVeranstaltung;
    }


    private static Veranstaltungsbeschreibung generateFakeBeschreibung() {
        Veranstaltungsbeschreibung fakeBeschreibung = new Veranstaltungsbeschreibung();
        fakeBeschreibung.setInhalte(faker.elderScrolls().quote());
        fakeBeschreibung.setLernergebnisse(faker.elderScrolls().quote());
        fakeBeschreibung.setHaeufigkeit("Einmal im leben");
        fakeBeschreibung.setSprache(faker.country().name());
        fakeBeschreibung.setLiteratur(faker.elderScrolls().quote());
        fakeBeschreibung.setVerwendbarkeit(faker.elderScrolls().quote());
        fakeBeschreibung.setVoraussetzungenBestehen(faker.elderScrolls().quote());
        return fakeBeschreibung;
    }

    private static void generateMultipleVeranstaltungsform(Veranstaltung fakeVeranstaltung) {
        double randomNumber = Math.random() * 5;
        Set<Veranstaltungsform> formen = new HashSet<>();
        for (int i = 0; i < randomNumber; i++) {
            formen.add(generateFakeVeranstaltungsform());
        }
        fakeVeranstaltung.setVeranstaltungsformen(formen);
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
