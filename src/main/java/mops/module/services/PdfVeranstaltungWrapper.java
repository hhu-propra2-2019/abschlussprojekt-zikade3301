package mops.module.services;

import static mops.module.services.PdfModulWrapper.getSafeString;
import static mops.module.services.PdfModulWrapper.safeAppend;

import java.util.Set;
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
        return getSafeString(veranstaltung.getLeistungspunkte());
    }

    public String getInhalte() {
        return getSafeString(veranstaltung.getBeschreibung().getInhalte());
    }

    public String getLernergebnisse() {
        return getSafeString(veranstaltung.getBeschreibung().getLernergebnisse());
    }

    public String getLiteratur() {
        return getSafeString(veranstaltung.getBeschreibung().getLiteratur());
    }

    public String getVerwendbarkeit() {
        return getSafeString(veranstaltung.getBeschreibung().getVerwendbarkeit());
    }

    public String getTeilnahmevoraussetzungen() {
        return getSafeString(veranstaltung.getVoraussetzungenTeilnahme());
    }

    public String getVoraussetzungenBestehen() {
        return getSafeString(veranstaltung.getBeschreibung().getVoraussetzungenBestehen());
    }

    public String getHaeufigkeit() {
        return getSafeString(veranstaltung.getBeschreibung().getHaeufigkeit());
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
        return veranstaltung.getZusatzfelder();
    }
}
