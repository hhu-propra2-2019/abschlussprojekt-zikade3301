package mops.module.services;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;
import mops.module.database.Veranstaltungsform;
import mops.module.database.Zusatzfeld;
import org.springframework.stereotype.Service;

@Service
public class FormService {

    /**
     * Erstellt aus eingegebenen Formulardaten ein entsprechendes Modul-Objekt.
     * @param allParams Alle Parameter des Modulformulars als Map, wobei jeweils der im Formular
     *                  übergebene name der Key und die Nutzereingabe als String der Value ist.
     * @return Modul-Objekt entsprechend der eingegebenen Daten.
     */
    public static Modul readModulFromParameterMap(Map<String, String> allParams) {

        //Einlesen der Formulardaten für modulspezifische Felder
        String titelDeutsch = allParams.get("titelDeutsch");
        String titelEnglisch = allParams.get("titelEnglisch");
        String studiengang = allParams.get("studiengang");
        String gesamtLeistungspunkte = allParams.get("gesamtLeistungspunkte");
        String modulbeauftragte = allParams.get("modulbeauftragte");
        Modulkategorie modulkategorie = Modulkategorie.valueOf(allParams.get("modulkategorie"));

        //Erstellen eines entsprechenden Modul-Objektes
        Modul modul = new Modul();
        modul.setTitelDeutsch(titelDeutsch);
        modul.setTitelEnglisch(titelEnglisch);
        modul.setStudiengang(studiengang);
        modul.setGesamtLeistungspunkte(gesamtLeistungspunkte);
        modul.setModulbeauftragte(modulbeauftragte);
        modul.setModulkategorie(modulkategorie);

        //Einlesen der Formulardaten für einzelne Veranstaltungen
        int veranstaltungsanzahl = Integer.parseInt(allParams.get("veranstaltungsanzahl"));
        Set<Veranstaltung> veranstaltungen = readVeranstaltungenFromParameterMap(allParams,
                veranstaltungsanzahl);
        modul.setVeranstaltungen(veranstaltungen);

        return modul;
    }

    private static Set<Veranstaltung> readVeranstaltungenFromParameterMap(
            Map<String, String> allParams, int veranstaltungsanzahl) {

        Set<Veranstaltung> veranstaltungen = new HashSet<>();
        for (int i = 0; i < veranstaltungsanzahl; i++) {
            int finalI = i + 1;
            Map<String, String> veranstaltungenParams = allParams.entrySet().stream()
                    .filter(e -> e.getKey().startsWith("veranstaltung" + finalI + "_"))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            Veranstaltung veranstaltung =
                    readSingleVeranstaltungFromParameterMap(veranstaltungenParams, finalI);
            veranstaltungen.add(veranstaltung);
        }
        return veranstaltungen;
    }

    private static Veranstaltung readSingleVeranstaltungFromParameterMap(
            Map<String, String> veranstaltungenParams, int index) {

        String titel = veranstaltungenParams.get("veranstaltung" + index + "_" + "titel");
        String leistungspunkte = veranstaltungenParams.get("veranstaltung" + index
                + "_" + "leistungspunkte");
        String voraussetzungenTeilnahme = veranstaltungenParams.get("veranstaltung" + index
                + "_" + "voraussetzungenTeilnahme");
        Veranstaltungsbeschreibung beschreibung =
                readVeranstaltungsbeschreibungFromParameterMap(veranstaltungenParams, index);
        Set<Veranstaltungsform> veranstaltungsformen =
                readVeranstaltungsformenFromParameterMap(veranstaltungenParams, index);
        Set<Zusatzfeld> zusatzfelder =
                readZusatzfelderFromParameterMap(veranstaltungenParams, index);

        Veranstaltung veranstaltung = new Veranstaltung();
        veranstaltung.setTitel(titel);
        veranstaltung.setLeistungspunkte(leistungspunkte);
        veranstaltung.setVoraussetzungenTeilnahme(voraussetzungenTeilnahme);
        veranstaltung.setZusatzfelder(zusatzfelder);
        veranstaltung.setVeranstaltungsformen(veranstaltungsformen);
        veranstaltung.setBeschreibung(beschreibung);
        return veranstaltung;
    }

    private static Veranstaltungsbeschreibung readVeranstaltungsbeschreibungFromParameterMap(
            Map<String, String> veranstaltungenParams, int index) {

        String inhalte = veranstaltungenParams.get("veranstaltung" + index
                + "_" + "inhalte");
        String lernergebnisse = veranstaltungenParams.get("veranstaltung" + index
                + "_" + "lernergebnisse");
        String literatur = veranstaltungenParams.get("veranstaltung" + index
                + "_" + "literatur");
        String verwendbarkeit = veranstaltungenParams.get("veranstaltung" + index
                + "_" + "verwendbarkeit");
        String voraussetzungenBestehen = veranstaltungenParams.get("veranstaltung" + index
                + "_" + "voraussetzungenBestehen");
        String haeufigkeit = veranstaltungenParams.get("veranstaltung" + index
                + "_" + "haeufigkeit");
        String sprache = veranstaltungenParams.get("veranstaltung" + index
                + "_" + "sprache");

        Veranstaltungsbeschreibung beschreibung = new Veranstaltungsbeschreibung();
        beschreibung.setInhalte(inhalte);
        beschreibung.setLernergebnisse(lernergebnisse);
        beschreibung.setLiteratur(literatur);
        beschreibung.setVerwendbarkeit(verwendbarkeit);
        beschreibung.setVoraussetzungenBestehen(voraussetzungenBestehen);
        beschreibung.setHaeufigkeit(haeufigkeit);
        beschreibung.setSprache(sprache);
        return beschreibung;
    }

    private static Set<Zusatzfeld> readZusatzfelderFromParameterMap(
            Map<String, String> veranstaltungenParams, int index) {

        String zusatzfeld1Titel = veranstaltungenParams.get("veranstaltung" + index
                + "_" + "zusatzfeld1_titel");
        String zusatzfeld1Inhalt = veranstaltungenParams.get("veranstaltung" + index
                + "_" + "zusatzfeld1_inhalt");
        String zusatzfeld2Titel = veranstaltungenParams.get("veranstaltung" + index
                + "_" + "zusatzfeld2_titel");
        String zusatzfeld2Inhalt = veranstaltungenParams.get("veranstaltung" + index
                + "_" + "zusatzfeld2_inhalt");

        Set<Zusatzfeld> zusatzfelder = new HashSet<>();
        if (!zusatzfeld1Titel.equals("") && !zusatzfeld1Inhalt.equals("")) {
            Zusatzfeld zusatzfeld = new Zusatzfeld();
            zusatzfeld.setTitel(zusatzfeld1Titel);
            zusatzfeld.setInhalt(zusatzfeld1Inhalt);
            zusatzfelder.add(zusatzfeld);
        }
        if (!zusatzfeld2Titel.equals("") && !zusatzfeld2Inhalt.equals("")) {
            Zusatzfeld zusatzfeld = new Zusatzfeld();
            zusatzfeld.setTitel(zusatzfeld2Titel);
            zusatzfeld.setInhalt(zusatzfeld2Inhalt);
            zusatzfelder.add(zusatzfeld);
        }
        return zusatzfelder;
    }

    private static Set<Veranstaltungsform> readVeranstaltungsformenFromParameterMap(
            Map<String, String> veranstaltungenParams, int index) {
        Set<Veranstaltungsform> veranstaltungsformen = new HashSet<>();
        for (int j = 0; j < 5; j++) {
            int finalJ = j + 1;
            Map<String, String> veranstaltungsformenParams = veranstaltungenParams
                    .entrySet().stream().filter(e -> e.getKey().startsWith("veranstaltung" + index
                            + "_" + "veranstaltungsform" + finalJ + "_"))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            String checked = veranstaltungsformenParams.get("veranstaltung" + index
                    + "_" + "veranstaltungsform" + finalJ + "_checked");
            if (checked != null) {
                Veranstaltungsform veranstaltungsform =
                        readSingleVeranstaltungsformFromParameterMap(veranstaltungsformenParams,
                                index, finalJ);
                if (!veranstaltungsform.getForm().equals("")) {
                    veranstaltungsformen.add(veranstaltungsform);
                }
            }
        }
        String veranstaltungsformTextareaChecked = veranstaltungenParams.get("veranstaltung"
                + index + "_" + "veranstaltungsform_textarea_checked");
        if (veranstaltungsformTextareaChecked != null) {
            String veranstaltungsformTextareaInhalt = veranstaltungenParams.get("veranstaltung"
                    + index + "_" + "veranstaltungsform_textarea_inhalt");
            Veranstaltungsform veranstaltungsform = new Veranstaltungsform();
            veranstaltungsform.setForm(veranstaltungsformTextareaInhalt);
            veranstaltungsformen.add(veranstaltungsform);
        }
        return veranstaltungsformen;
    }

    private static Veranstaltungsform readSingleVeranstaltungsformFromParameterMap(
            Map<String, String> veranstaltungsformenParams,
            int veranstaltungsIndex, int veranstaltungsformIndex) {
        Veranstaltungsform veranstaltungsform = new Veranstaltungsform();
        String form = veranstaltungsformenParams.get("veranstaltung" + veranstaltungsIndex
                + "_" + "veranstaltungsform" + veranstaltungsformIndex + "_form");
        String sws = veranstaltungsformenParams.get("veranstaltung" + veranstaltungsIndex
                + "_" + "veranstaltungsform" + veranstaltungsformIndex + "_sws");
        veranstaltungsform.setForm(form);
        if (sws != null) {
            if (!sws.equals("")) {
                veranstaltungsform.setSemesterWochenStunden(Integer.parseInt(sws));
            }
        }
        return veranstaltungsform;
    }

}
