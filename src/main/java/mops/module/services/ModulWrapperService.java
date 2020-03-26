package mops.module.services;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import mops.module.database.Modul;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsform;
import mops.module.database.Zusatzfeld;
import mops.module.wrapper.ModulWrapper;
import org.springframework.stereotype.Service;

@Service
public class ModulWrapperService {

    public static int VERANSTALTUNGSFORMEN_PRO_VERANSTALTUNG = 6;
    public static int ZUSATZFELDER_PRO_VERANSTALTUNG = 2;

    /**
     * Entpackt aus einem ModulWrapper-Objekt ein Modul-Objekt.
     * @param modulWrapper Im Formular empfangener ModulWrapper.
     * @return Ein entsprechendes Modul-Objekt.
     */
    public static Modul readModulFromWrapper(ModulWrapper modulWrapper) {
        for (int i = 0; i < modulWrapper.getVeranstaltungen().size(); i++) {
            modulWrapper.getVeranstaltungen().get(i).setVeranstaltungsformen(
                    new HashSet<>(modulWrapper.getVeranstaltungsformen()[i]));
            modulWrapper.getVeranstaltungen().get(i).setZusatzfelder(
                    new HashSet<>(modulWrapper.getZusatzfelder()[i]));
        }
        Set<Veranstaltung> veranstaltungenSet = new HashSet<>(modulWrapper.getVeranstaltungen());

        Modul modul = modulWrapper.getModul();
        modul.setVeranstaltungen(veranstaltungenSet);
        modul.refreshMapping();
        return modul;
    }

    /**
     * Erstellt einen leeren ModulWrapper für ein noch nicht exisitierendes Modul, der entsprechende
     * Listen von neuen Veranstaltung-, Veranstaltungsform- und Zusatzfeld-Instanzen enthält,
     * damit über diese im Frontend iteriert und ihre Felder befüllt werden können.
     * @param veranstaltungsanzahl Anzahl der Veranstaltungen in dem Modul.
     * @return Das entsprechende ModulWrapper-Objekt.
     */
    @SuppressWarnings("unchecked")
    public static ModulWrapper initializeEmptyWrapper(int veranstaltungsanzahl) {

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
            for (int j = 0; j < VERANSTALTUNGSFORMEN_PRO_VERANSTALTUNG; j++) {
                Veranstaltungsform vf = new Veranstaltungsform();
                modulWrapper.veranstaltungsformen[i].add(vf);
            }
            for (int j = 0; j < ZUSATZFELDER_PRO_VERANSTALTUNG; j++) {
                Zusatzfeld zf = new Zusatzfeld();
                modulWrapper.zusatzfelder[i].add(zf);
            }
        }

        return modulWrapper;
    }

    /**
     * Erstellt einen ModulWrapper für ein bereits exisitierendes Modul, der auf die
     * für das Formular festgelegte Größe vergrößerte Listen (s.o.) enthält,
     * damit über diese im Frontend iteriert und ihre Felder befüllt werden können.
     * @param modul Das existierende Modul.
     * @return Das entsprechende ModulWrapper-Objekt, in dem die Listen auf die festgelegte
     *         Größe durch leere Instanzen erhöht wurden, damit im Frontend die alten
     *         Listeneinträge durch leere Felder auf die festgelegte Gesamtanzahl vergrößert werden.
     */
    @SuppressWarnings("unchecked")
    public static ModulWrapper initializePrefilledWrapper(Modul modul) {

        ModulWrapper modulWrapper = new ModulWrapper(modul,
                null, null, null);

        if (modul.getVeranstaltungen() != null) {
            modulWrapper.veranstaltungen = new LinkedList<>(modul.getVeranstaltungen());
        } else {
            modulWrapper.veranstaltungen = new LinkedList<>();
        }
        modulWrapper.veranstaltungsformen = new LinkedList[modulWrapper.veranstaltungen.size()];
        modulWrapper.zusatzfelder = new LinkedList[modulWrapper.veranstaltungen.size()];
        for (int i = 0; i < modulWrapper.veranstaltungen.size(); i++) {
            modulWrapper.veranstaltungsformen[i] =
                    new LinkedList<>(modulWrapper.veranstaltungen.get(i).getVeranstaltungsformen());
            modulWrapper.zusatzfelder[i] =
                    new LinkedList<>(modulWrapper.veranstaltungen.get(i).getZusatzfelder());
            while (modulWrapper.veranstaltungsformen[i].size()
                    < VERANSTALTUNGSFORMEN_PRO_VERANSTALTUNG) {
                Veranstaltungsform vf = new Veranstaltungsform();
                modulWrapper.veranstaltungsformen[i].add(vf);
            }
            while (modulWrapper.zusatzfelder[i].size() < ZUSATZFELDER_PRO_VERANSTALTUNG) {
                Zusatzfeld z = new Zusatzfeld();
                modulWrapper.zusatzfelder[i].add(z);
            }
        }
        return modulWrapper;
    }

}
