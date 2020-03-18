package mops.module.modulGenerator;

import com.github.javafaker.Faker;
import java.util.Set;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;
import mops.module.database.Zusatzfeld;

public class ModulFaker {
    // modul
    private static Faker faker = new Faker();
    private static final String[] titelDeutsch = {"Rechnerarchitektur"};
    private static final String[] titelEnglish = {"Computer Architecture"};
    // veranstaltung Entität
    private static final String[] modulbeauftragte={faker.harryPotter().character(), faker.harryPotter().character()};
    private static final String[] gesamtLeistungspunkte = {"10CP"};
    private static final String[] studiengang = {"Bachelor-StudiengangInformatik"};
    private static final Modulkategorie[] modulkategorrie = {Modulkategorie.PFLICHT_INFO};
    private static final Boolean[] sichtbar = {true};
    // zusatzfelder Entität
    //
    //// veranstaltung
    private static final String veranstaltungTitel[] = {faker.pokemon().name()};
    private static final String lehrende[] = {faker.harryPotter().character()};
    private static final String leistungspunkte[] = {"5CP"};
    private static final String veranstaltungsFormen[] = {faker.elderScrolls().quote()};
    //// beshcreibung embedded
    //// voraussetzungenTeilnahme -> mehrere veranstaltungTitels
    private static final String semester[] = {"WS19","SS19","WS/SS20"};
    //
    ////// Veranstaltungsbeschreibung
    private static final String inhalt[] = {faker.elderScrolls().quote()};
    private static final String lernergebnisse[] = {faker.elderScrolls().quote()};
    private static final String literatur[] = {faker.book().title()};
    private static final String verwendbarkeit[] = {faker.backToTheFuture().quote(),faker.backToTheFuture().quote()};
    private static final String voraussetzungenBestehen[] = {faker.job().keySkills()};
    private static final String haeufigkeit[] = {"Die Vorlesung „Rechnerarchitektur“ mit zugehöriger Übung wird jedes Sommersemester angeboten."};
    private static final String sprache[] = {"Deutsch"};
    //// Zusatzfeld
    private static final String zusatzfeldtitel[] = {faker.book().title()};
    private static final String zusatzfeldInhalt[] = {faker.chuckNorris().fact()};

    //
    public static Modul generateFakeModul() {
        Modul fakeModul = new Modul();
        generateMultipleVeranstaltungen(fakeModul.getVeranstaltungen());
        fakeModul.setTitelDeutsch(chooseRandom(titelDeutsch));
        fakeModul.setTitelEnglisch(chooseRandom(titelEnglish));
        fakeModul.setGesamtLeistungspunkte(chooseRandom(gesamtLeistungspunkte));
        fakeModul.setStudiengang(chooseRandom(studiengang));
        fakeModul.setModulkategorie(chooseRandom(modulkategorrie));
        fakeModul.setSichtbar(chooseRandom(sichtbar));
        chooseRandom(fakeModul.getModulbeauftragte(), modulbeauftragte);
        Zusatzfeld zusatzfeld = new Zusatzfeld();
        zusatzfeld.setTitel(chooseRandom(zusatzfeldtitel));
        zusatzfeld.setInhalt(chooseRandom(zusatzfeldInhalt));
        fakeModul.getZusatzfelder().add(zusatzfeld);
        return fakeModul;
    }

    private static void generateMultipleVeranstaltungen(Set<Veranstaltung> veranstaltungen) {
        double randomNumber = Math.random() * 5;
        for (int i = 0; i < randomNumber; i++) {
            veranstaltungen.add(generateFakeVeranstaltung());
        }
    }

    private static Veranstaltung generateFakeVeranstaltung() {
        Veranstaltung fakeVeranstaltung = new Veranstaltung();
        Veranstaltungsbeschreibung fakeBeshcreibung = generateFakeBeschreibung();
        //
        fakeVeranstaltung.setTitel(chooseRandom(veranstaltungTitel));
        fakeVeranstaltung.setLeistungspunkte(chooseRandom(leistungspunkte));
        fakeVeranstaltung.setBeschreibung(fakeBeshcreibung);
        //
        chooseRandom(fakeVeranstaltung.getLehrende(), lehrende);
        chooseRandom(fakeVeranstaltung.getVeranstaltungsformen(), veranstaltungsFormen);
        chooseRandom(fakeVeranstaltung.getVoraussetzungenTeilnahme(), veranstaltungTitel);
        chooseRandom(fakeVeranstaltung.getSemester(), semester);
        //
        return fakeVeranstaltung;
    }

    private static Veranstaltungsbeschreibung generateFakeBeschreibung() {
        Veranstaltungsbeschreibung fakeBeshcreibung = new Veranstaltungsbeschreibung();
        //
        fakeBeshcreibung.setInhalte(chooseRandom(inhalt));
        fakeBeshcreibung.setLernergebnisse(chooseRandom(lernergebnisse));
        fakeBeshcreibung.setHaeufigkeit(chooseRandom(haeufigkeit));
        fakeBeshcreibung.setSprache(chooseRandom(sprache));
        //
        chooseRandom(fakeBeshcreibung.getLiteratur(), literatur);
        chooseRandom(fakeBeshcreibung.getVerwendbarkeit(), verwendbarkeit);
        chooseRandom(fakeBeshcreibung.getVoraussetzungenBestehen(), voraussetzungenBestehen);
        //
        return fakeBeshcreibung;
    }

    private static <T extends Object> T chooseRandom(T[] objects) {
        return objects[(int) Math.round(Math.random() * objects.length)];
    }

    private static <T extends Object> void chooseRandom(Set<T> set, T[] objects) {
        double randomNumber = Math.random() * 6;
        for (int i = 0; i < randomNumber; i++) {
            set.add(objects[i]);
        }
    }

}
