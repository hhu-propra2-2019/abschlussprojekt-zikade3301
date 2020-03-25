package mops.module.services;

import lombok.RequiredArgsConstructor;
import mops.module.database.Modul;

@RequiredArgsConstructor
public class PdfModulWrapper {

    private final Modul modul;

    public String getStudiengang() {
        return getSafeString(modul.getStudiengang()) + "\n";
    }

    public String getModulbeauftragte() {
        return getSafeString(modul.getModulbeauftragte()) + "\n";
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
