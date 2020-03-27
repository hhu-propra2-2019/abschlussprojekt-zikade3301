package mops.module.services;

import static mops.module.services.PdfModulWrapper.getSafeString;
import static mops.module.services.PdfModulWrapper.safeAppend;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsform;
import mops.module.database.Zusatzfeld;

/**
 * Formatiert die Daten aus dem Veranstaltungsobjekt passend für das PDF-Template.
 */
@RequiredArgsConstructor
public class PdfVeranstaltungWrapper {

    private final Veranstaltung veranstaltung;

    /**
     * Formatiert "Lehrveranstaltungs"-Strings wie im Modulhandbuch.
     *
     * @return Lehrveranstaltungs-Strings
     */
    public Set<String> getLehrveranstaltungen() {
        Set<String> lehrveranstaltungen = new HashSet<>();
        for (Veranstaltungsform veranstaltungsform : veranstaltung.getVeranstaltungsformen()) {
            String veranstaltungsformString = "";
            veranstaltungsformString = safeAppend(veranstaltungsformString,
                    veranstaltungsform.getForm());

            if (veranstaltungsform.getSemesterWochenStunden() > 0) {
                veranstaltungsformString += ", " + veranstaltungsform.getSemesterWochenStunden()
                        + " SWS";
            }
            lehrveranstaltungen.add(veranstaltungsformString);
        }
        return lehrveranstaltungen;
    }

    public String getTitel() {
        return getSafeString(
                veranstaltung.getTitel());
    }

    public String getLeistungspunkte() {
        return getSafeString(
                veranstaltung.getLeistungspunkte());
    }

    public String getInhalte() {
        return HtmlService.markdownToHtml(getSafeString(
                veranstaltung.getBeschreibung().getInhalte()));
    }

    public String getLernergebnisse() {
        return HtmlService.markdownToHtml(getSafeString(
                veranstaltung.getBeschreibung().getLernergebnisse()));
    }

    public String getLiteratur() {
        return HtmlService.markdownToHtml(getSafeString(
                veranstaltung.getBeschreibung().getLiteratur()));
    }

    public String getVerwendbarkeit() {
        return HtmlService.markdownToHtml(getSafeString(
                veranstaltung.getBeschreibung().getVerwendbarkeit()));
    }

    public String getTeilnahmevoraussetzungen() {
        return HtmlService.markdownToHtml(getSafeString(
                veranstaltung.getVoraussetzungenTeilnahme()));
    }

    public String getVoraussetzungenBestehen() {
        return HtmlService.markdownToHtml(getSafeString(
                veranstaltung.getBeschreibung().getVoraussetzungenBestehen()));
    }

    public String getHaeufigkeit() {
        return getSafeString(
                veranstaltung.getBeschreibung().getHaeufigkeit());
    }

    /**
     * Gibt mit Markdown formatierte Zusatzfelder zurück.
     *
     * @return Set von Zusatzfeldern
     */
    public Set<Zusatzfeld> getZusatzfelder() {
        return veranstaltung.getZusatzfelder()
                .stream()
                .map(zusatzfeld -> zusatzfeldEnableMarkdown(zusatzfeld))
                .collect(Collectors.toSet());
    }

    private static Zusatzfeld zusatzfeldEnableMarkdown(Zusatzfeld zusatzfeld) {
        Zusatzfeld neuesZusatzfeld = new Zusatzfeld();
        neuesZusatzfeld.setTitel(zusatzfeld.getTitel());
        neuesZusatzfeld.setInhalt(HtmlService.markdownToHtml(zusatzfeld.getInhalt()));
        return neuesZusatzfeld;
    }
}
