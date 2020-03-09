package mops.module.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import mops.module.database.Modul;
import mops.module.database.Veranstaltung;
import org.springframework.stereotype.Service;

@Service
public class UpdateModulService {

    AenderungsRepository aenderungsRepo;

    public UpdateModulService(AenderungsRepository aenderungsRepository) {
        this.aenderungsRepo = aenderungsRepo;
    }

    public List<Modul> getAlleModule() {
        List<Modul> module = new List<Modul>();
        List<Aenderung> aenderungen = aenderungsRepo.findAll();

        for(Aenderung aenderung : aenderungen) {
            
        }
    }

    public static Aenderung calculateDiffs(Modul altesModul, Modul neuesModul) {
        Aenderung aenderung = new Aenderung();
        aenderung.createDate = LocalDateTime.now();
        aenderung.approveDate = null;
        aenderung.ModulID = altesModul.ID;
        Map<Integer, Kategorie> kategorien = new HashMap<Integer, Kategorie>();
        aenderung.kategorien = kategorien;

        for (Map.Entry<Integer, Kategorie> entry : neuesModul.kategorien.entrySet()) {
            if (altesModul.kategorien.get(entry.getKey()).compareTo(entry.getValue()) != 0) {
                kategorien.put(entry.getKey(), entry.getValue());
            }
        }
        /*
        forEach Eigenschaft {
            if(altesModul.Eigenschaft != neuesModul.Eigenschaft) {
                aenderungen.Add(new Aenderung(Eigenschaft, neuesModul.Eigenschaft));
            }
        }
         */
        return aenderung;
    }

    private static List<Aenderung> filterAenderungenUntil(List<Aenderung> aenderungen, LocalDateTime untilWhen) {
        List<Aenderung> filteredAenderungen = new List<Aenderung>();

        for (Aenderung aenderung : aenderungen) {
            if (aenderung.approveDate != null && aenderung.approveDate.isBefore(untilWhen)) {
                filteredAenderungen.add(aenderung);
            }
        }

        return filteredAenderungen;
    }

    public boolean updateModul(Modul neuesModul) {
        if (check(neuesModul)) {
            //OK

            Modul altesModul = buildAltesModul(neuesModul, LocalDateTime.now());
            Aenderung aenderung = calculateDiffs(altesModul, neuesModul);
            aenderungsRepo.updateModul(aenderung);
        } else {
            //Not OK
        }
    }

    private Modul buildAltesModul(Modul modul, LocalDateTime untilWhen) {
        Modul altesModul = new Modul();

        List<Aenderung> modulAenderungen = aenderungsRepo.findById(modul.ID);
        modulAenderungen = filterAenderungenUntil(modulAenderungen, untilWhen);

        for (Aenderung aenderung : modulAenderungen) {
            altesModul.applyAenderung(aenderung);
        }
        return altesModul;
    }

    //!!! Klasse: Modul
    public void applyAenderung(Aenderung aenderung) {
        Map<Integer, Kategorie> kategorieAenderungen = aenderung.kategorien;
        for (Map.Entry<Integer, Kategorie> entry : kategorieAenderungen.entrySet()) {
            kategorien.put(entry.getKey(), entry.getValue());
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
