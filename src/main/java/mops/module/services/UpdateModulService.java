package mops.module.services;

import mops.module.database.Modul;
import mops.module.database.Veranstaltung;
import mops.module.repositories.AntragRepository;
import mops.module.repositories.ModulRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateModulService {

    AntragRepository antragRepo;
    ModulRepository modulRepo;

    public UpdateModulService(AntragRepository antragRepo, ModulRepository modulRepo) {
        this.antragRepo = antragRepo;
        this.modulRepo = modulRepo;
    }

    public boolean updateModul(Modul modul) {
        if (check(modul)) {
            //OK

            Antrag antrag = modulRepo.getCorrespondingAntrag(modul);
            antragRepo.updateModul(antrag);
        } else {
            //Not OK
        }
    }

    private boolean check(Modul modul) {
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
