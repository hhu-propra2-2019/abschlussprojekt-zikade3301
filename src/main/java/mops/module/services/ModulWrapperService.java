package mops.module.services;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import mops.module.controller.ModulWrapper;
import mops.module.database.Modul;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsform;
import mops.module.database.Zusatzfeld;
import org.springframework.stereotype.Service;

@Service
public class ModulWrapperService {

    public static Modul readModulFromWrapper(ModulWrapper modulWrapper) {
        for (int i = 0; i < modulWrapper.getVeranstaltungen().size(); i++) {
            modulWrapper.getVeranstaltungen().get(i).setVeranstaltungsformen(
                    new HashSet<>(modulWrapper.getVeranstaltungsformen()[i]));
            modulWrapper.getVeranstaltungen().get(i).setZusatzfelder(
                    new HashSet<>(modulWrapper.getZusatzfelder()[i]));
        }
        Set<Veranstaltung> veranstaltungenSet = new HashSet<>(modulWrapper.getVeranstaltungen());

        Modul modul = modulWrapper.getModul();
        //modul.getVeranstaltungen().clear();     //TODO: verwaiste Veranstaltungen entfernen!
        modul.setVeranstaltungen(veranstaltungenSet);
        modul.refreshMapping();
        return modul;
    }

    @SuppressWarnings("unchecked")
    public static ModulWrapper initializeEmptyWrapper(int veranstaltungsanzahl) {
        int veranstaltungsformenProVeranstaltung = 6;
        int zusatzfelderProVeranstaltung = 2;

        Modul modul = new Modul();
        ModulWrapper modulWrapper = new ModulWrapper(modul,
                null, null, null);
        modulWrapper.veranstaltungen = new LinkedList<>();
        modulWrapper.veranstaltungsformen = new LinkedList[veranstaltungsanzahl];
        modulWrapper.zusatzfelder = new LinkedList[veranstaltungsanzahl];

        for (int i = 0; i < veranstaltungsanzahl; i++) {
            modulWrapper.veranstaltungsformen[i] = new LinkedList<>();
            modulWrapper.zusatzfelder[i] = new LinkedList<>();
            Veranstaltung veranstaltung = new Veranstaltung();
            modulWrapper.veranstaltungen.add(veranstaltung);
            for (int j = 0; j < veranstaltungsformenProVeranstaltung; j++) {
                Veranstaltungsform vf = new Veranstaltungsform();
                modulWrapper.veranstaltungsformen[i].add(vf);
            }
            for (int j = 0; j < zusatzfelderProVeranstaltung; j++) {
                Zusatzfeld zf = new Zusatzfeld();
                modulWrapper.zusatzfelder[i].add(zf);
            }
        }

        return modulWrapper;
    }

    @SuppressWarnings("unchecked")
    public static ModulWrapper initializePrefilledModulWrapper(Modul modul) {

        int veranstaltungsformenProVeranstaltung = 6;
        int zusatzfelderProVeranstaltung = 2;

        ModulWrapper modulWrapper = new ModulWrapper(modul,
                null, null, null);

        modulWrapper.veranstaltungen = new LinkedList<>(modul.getVeranstaltungen());
        modulWrapper.veranstaltungsformen = new LinkedList[modulWrapper.veranstaltungen.size()];
        modulWrapper.zusatzfelder = new LinkedList[modulWrapper.veranstaltungen.size()];
        for (int i = 0; i < modulWrapper.veranstaltungen.size(); i++) {
            modulWrapper.veranstaltungsformen[i] =
                    new LinkedList<>(modulWrapper.veranstaltungen.get(i).getVeranstaltungsformen());
            modulWrapper.zusatzfelder[i] = new LinkedList<>(modulWrapper.veranstaltungen.get(i).getZusatzfelder());
            while (modulWrapper.veranstaltungsformen[i].size() < veranstaltungsformenProVeranstaltung) {
                Veranstaltungsform vf = new Veranstaltungsform();
                modulWrapper.veranstaltungsformen[i].add(vf);
            }
            while (modulWrapper.zusatzfelder[i].size() < zusatzfelderProVeranstaltung) {
                Zusatzfeld z = new Zusatzfeld();
                modulWrapper.zusatzfelder[i].add(z);
            }
        }
        return modulWrapper;
    }

}
