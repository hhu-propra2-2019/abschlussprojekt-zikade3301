package mops.module.services;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;

/**
 * Formatiert die Daten aus dem Modulobjekt passend für das PDF-Template.
 */
@RequiredArgsConstructor
public class PdfModulWrapper {

    private final Modul modul;

    public String getId() {
        return modul.getId().toString();
    }

    public Modulkategorie getModulkategorie() {
        return modul.getModulkategorie();
    }

    public String getTitelDeutsch() {
        return getSafeString(modul.getTitelDeutsch());
    }

    public String getTitelEnglisch() {
        return getSafeString(modul.getTitelEnglisch());
    }

    public String getStudiengang() {
        return getSafeString(modul.getStudiengang());
    }

    public String getModulbeauftragte() {
        return getSafeString(modul.getModulbeauftragte());
    }

    /**
     * Gibt die zu gehörigen PdfVeranstaltungsWrapper zurück.
     * @return Set von PdfVeranstaltungsWrappern
     */
    public Set<PdfVeranstaltungWrapper> getVeranstaltungen() {
        HashSet<PdfVeranstaltungWrapper> set = new HashSet<>();
        modul.getVeranstaltungen().forEach(v -> set.add(new PdfVeranstaltungWrapper(v)));
        return set;
    }

    static String safeAppend(String base, String toAppend) {
        if (isNullOrEmpty(toAppend)) {
            return base;
        }
        return base + toAppend;
    }

    static boolean isNullOrEmpty(String string) {
        return (string == null || string.isEmpty());
    }

    static String getSafeString(String string) {
        if (isNullOrEmpty(string)) {
            return "—";
        }
        return string;
    }
}
