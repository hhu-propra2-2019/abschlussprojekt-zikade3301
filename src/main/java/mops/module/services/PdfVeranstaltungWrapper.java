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

@RequiredArgsConstructor
public class PdfVeranstaltungWrapper {

    private final Veranstaltung veranstaltung;

    public Set<String> getLehrveranstaltungen() {
        Set<String> lehrveranstaltungen = new HashSet<>();
        for (Veranstaltungsform veranstaltungsform : veranstaltung.getVeranstaltungsformen()) {
            String veranstaltungsformString = "";
            veranstaltungsformString = safeAppend(veranstaltungsformString, veranstaltungsform.getForm());

            if (veranstaltungsform.getSemesterWochenStunden() > 0) {
                veranstaltungsformString += ", " + veranstaltungsform.getSemesterWochenStunden() + " SWS";
            }
            lehrveranstaltungen.add(veranstaltungsformString);
        }
        return lehrveranstaltungen;
    }

    public String getTitel() {
        return getSafeString(veranstaltung.getTitel());
    }

    public String getLeistungspunkte() {
        return getSafeString(veranstaltung.getLeistungspunkte());
    }

    public String getInhalte() {
        return HtmlService.markdownToHtml(getSafeString(veranstaltung.getBeschreibung().getInhalte()));
    }

    public String getLernergebnisse() {
        return HtmlService.markdownToHtml(getSafeString(veranstaltung.getBeschreibung().getLernergebnisse()));
    }

    public String getLiteratur() {
        return HtmlService.markdownToHtml(getSafeString(veranstaltung.getBeschreibung().getLiteratur()));
    }

    public String getVerwendbarkeit() {
        return HtmlService.markdownToHtml(getSafeString(veranstaltung.getBeschreibung().getVerwendbarkeit()));
    }

    public String getTeilnahmevoraussetzungen() {
        return HtmlService.markdownToHtml(getSafeString(veranstaltung.getVoraussetzungenTeilnahme()));
    }

    public String getVoraussetzungenBestehen() {
        return HtmlService.markdownToHtml(getSafeString(veranstaltung.getBeschreibung().getVoraussetzungenBestehen()));
    }

    public String getHaeufigkeit() {
        return HtmlService.markdownToHtml(getSafeString(veranstaltung.getBeschreibung().getHaeufigkeit()));
    }

    public Set<Zusatzfeld> getZusatzfelder() {
        /*String zusatzString = "";
        for (Zusatzfeld zusatzfeld : veranstaltung.getZusatzfelder()) {
            if (zusatzfeld != null && !isNullOrEmpty(zusatzfeld.getTitel()) && !isNullOrEmpty(zusatzfeld.getInhalt())) {
                zusatzString += "### " + zusatzfeld.getTitel() + "\n";
                zusatzString += zusatzfeld.getInhalt() + "\n";
            }
        }
        return zusatzString;*/
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
