package mops.module.services;

import mops.module.database.Modulbeschreibung;

public class UpdateModulService {

    AntragRepository antragRepo;

    public UpdateModulService(AntragRepository antragRepo) {
        this.antragRepo = antragRepo;
    }

    Antrag check(Modul modul) {
        checkMeta(modul.meta);
        checkBeschreibung(modul.beschreibung);
    }

    private boolean checkMeta(MetaModul metaModul) {
        boolean titelENBool = checkTitel(metaModul.titelEN);
        boolean titelDEBool = checkTitel(metaModul.titelDE);
        boolean varanstaltunegnBool = metaModul.veranstaltungen.stream()
                .forEach(this::checkVeranstaltungen);


    }

    private boolean checkTitel(String titel) {
        if (tite.length == 0) {
            return false;
        }
        return true;
    }

    private boolean checkBeschreibung(Modulbeschreibung beschreibung) {

        return true;
    }

    private boolean checkVeranstaltungen(Veranstaltung veranstaltung) {

        return true;
    }
}
