package mops.module.services;

import static mops.module.services.PdfModulWrapper.getSafeString;
import static mops.module.services.PdfModulWrapper.isNullOrEmpty;
import static mops.module.services.PdfModulWrapper.safeAppend;

import lombok.RequiredArgsConstructor;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsform;
import mops.module.database.Zusatzfeld;

@RequiredArgsConstructor
public class PdfVeranstaltungWrapper {

    private final Veranstaltung veranstaltung;

    public String getLehrveranstaltungen() {
        String veranstaltungsString = "";
        for (Veranstaltungsform veranstaltungsform : veranstaltung.getVeranstaltungsformen()) {
            String veranstaltungsformString = "";
            veranstaltungsformString = safeAppend(veranstaltungsformString, veranstaltungsform.getForm());

            if (veranstaltungsform.getSemesterWochenStunden() > 0) {
                veranstaltungsformString += ", " + veranstaltungsform.getSemesterWochenStunden() + " SWS";
            }
            veranstaltungsString += veranstaltungsformString;
        }
        return veranstaltungsString;
    }

    public String getLeistungspunkte() {
        return getSafeString(veranstaltung.getLeistungspunkte()) + "\n";
    }

    public String getInhalte() {
        return getSafeString(veranstaltung.getBeschreibung().getInhalte()) + "\n";
    }

    public String getLernergebnisse() {
        return getSafeString(veranstaltung.getBeschreibung().getLernergebnisse()) + "\n";
    }

    public String getLiteratur() {
        return getSafeString(veranstaltung.getBeschreibung().getLiteratur()) + "\n";
    }

    public String getVerwendbarkeit() {
        return getSafeString(veranstaltung.getBeschreibung().getVerwendbarkeit()) + "\n";
    }

    public String getTeilnahmevoraussetzungen() {
        return getSafeString(veranstaltung.getVoraussetzungenTeilnahme()) + "\n";
    }

    public String getVoraussetzungenBestehen() {
        return getSafeString(veranstaltung.getBeschreibung().getVoraussetzungenBestehen()) + "\n";
    }

    public String getHaeufigkeit() {
        return getSafeString(veranstaltung.getBeschreibung().getHaeufigkeit()) + "\n";
    }

    public String getZusatzfelderString() {
        String zusatzString = "";
        for (Zusatzfeld zusatzfeld : veranstaltung.getZusatzfelder()) {
            if (zusatzfeld != null && !isNullOrEmpty(zusatzfeld.getTitel()) && !isNullOrEmpty(zusatzfeld.getInhalt())) {
                zusatzString += "### " + zusatzfeld.getTitel() + "\n";
                zusatzString += zusatzfeld.getInhalt() + "\n";
            }
        }
        return zusatzString;
    }
}
