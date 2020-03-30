package mops.module.services;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import mops.module.database.Antrag;
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
        Set<Veranstaltung> veranstaltungenInWrapper = getVeranstaltungenAsSet(modulWrapper);
        removeInvalidListEntriesFromVeranstaltungenSet(veranstaltungenInWrapper);
        Modul modul = modulWrapper.getModul();
        modul.setVeranstaltungen(veranstaltungenInWrapper);
        modul.refreshMapping();
        return modul;
    }

    private static void removeInvalidListEntriesFromVeranstaltungenSet(
            Set<Veranstaltung> veranstaltungenInWrapper) {
        for (Veranstaltung v : veranstaltungenInWrapper) {
            if (v.getVeranstaltungsformen() != null) {
                v.getVeranstaltungsformen().removeIf(vf -> vf.getForm() == null);
                v.getVeranstaltungsformen().removeIf(vf -> vf.getForm().equals(""));
            }
            if (v.getZusatzfelder() != null) {
                v.getZusatzfelder().removeIf(vf -> vf.getTitel() == null || vf.getInhalt() == null);
                v.getZusatzfelder()
                        .removeIf(zf -> zf.getTitel().equals("") || zf.getInhalt().equals(""));
            }
        }
    }

    private static Set<Veranstaltung> getVeranstaltungenAsSet(ModulWrapper modulWrapper) {
        for (int i = 0; i < modulWrapper.getVeranstaltungen().size(); i++) {
            modulWrapper.getVeranstaltungen().get(i).setVeranstaltungsformen(
                    new HashSet<>(modulWrapper.getVeranstaltungsformen()[i]));
            modulWrapper.getVeranstaltungen().get(i).setZusatzfelder(
                    new HashSet<>(modulWrapper.getZusatzfelder()[i]));
        }
        return new HashSet<>(modulWrapper.getVeranstaltungen());
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

        List<Veranstaltung> veranstaltungen = new LinkedList<>();
        List<Veranstaltungsform>[] veranstaltungsformen = new LinkedList[veranstaltungsanzahl];
        List<Zusatzfeld>[] zusatzfelder = new LinkedList[veranstaltungsanzahl];

        addEmptyVeranstaltungen(veranstaltungen, veranstaltungsanzahl);
        addEmptyVeranstaltungsformen(veranstaltungsformen, veranstaltungsanzahl);
        addEmptyZusatzfelder(zusatzfelder, veranstaltungsanzahl);

        return new ModulWrapper(new Modul(), veranstaltungen, veranstaltungsformen, zusatzfelder);
    }

    private static void addEmptyVeranstaltungen(
            List<Veranstaltung> veranstaltungen, int veranstaltungsanzahl) {
        for (int i = 0; i < veranstaltungsanzahl; i++) {
            veranstaltungen.add(new Veranstaltung());
        }
    }


    private static void addEmptyVeranstaltungsformen(
            List<Veranstaltungsform>[] veranstaltungsformen, int veranstaltungsanzahl) {
        for (int i = 0; i < veranstaltungsanzahl; i++) {
            veranstaltungsformen[i] = new LinkedList<>();
            for (int j = 0; j < VERANSTALTUNGSFORMEN_PRO_VERANSTALTUNG; j++) {
                veranstaltungsformen[i].add(new Veranstaltungsform());
            }
        }
    }

    private static void addEmptyZusatzfelder(
            List<Zusatzfeld>[] zusatzfelder, int veranstaltungsanzahl) {
        for (int i = 0; i < veranstaltungsanzahl; i++) {
            zusatzfelder[i] = new LinkedList<>();
            for (int j = 0; j < ZUSATZFELDER_PRO_VERANSTALTUNG; j++) {
                zusatzfelder[i].add(new Zusatzfeld());
            }
        }
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

        List<Veranstaltung> veranstaltungen = new LinkedList<>(modul.getVeranstaltungen());
        sortVernstaltungListById(veranstaltungen);
        List<Veranstaltungsform>[] veranstaltungsformen = new List[veranstaltungen.size()];
        List<Zusatzfeld>[] zusatzfelder = new List[veranstaltungen.size()];

        fillUpWithEmptyVeranstaltungsformen(veranstaltungsformen, veranstaltungen);
        fillUpWithEmptyZusatzfelder(zusatzfelder, veranstaltungen);

        return new ModulWrapper(modul, veranstaltungen, veranstaltungsformen, zusatzfelder);
    }

    private static void fillUpWithEmptyVeranstaltungsformen(
            List<Veranstaltungsform>[] veranstaltungsformen, List<Veranstaltung> veranstaltungen) {
        for (int i = 0; i < veranstaltungen.size(); i++) {

            veranstaltungsformen[i] = new LinkedList<>(veranstaltungen.get(i).getVeranstaltungsformen());

            sortVernstaltungsformListById(veranstaltungsformen[i]);
            while (veranstaltungsformen[i].size() < VERANSTALTUNGSFORMEN_PRO_VERANSTALTUNG) {
                veranstaltungsformen[i].add(new Veranstaltungsform());
            }
        }
    }

    private static void fillUpWithEmptyZusatzfelder(
            List<Zusatzfeld>[] zusatzfelder, List<Veranstaltung> veranstaltungen) {
        for (int i = 0; i < veranstaltungen.size(); i++) {
            zusatzfelder[i] = new LinkedList<>(veranstaltungen.get(i).getZusatzfelder());
            sortZusatzfeldListById(zusatzfelder[i]);
            while (zusatzfelder[i].size() < ZUSATZFELDER_PRO_VERANSTALTUNG) {
                zusatzfelder[i].add(new Zusatzfeld());
            }
        }
    }

    private static void sortVernstaltungListById(
            List<Veranstaltung> veranstaltungen) {
        veranstaltungen.sort(Comparator.nullsLast(Comparator.comparing(Veranstaltung::getId, Comparator.nullsLast(Comparator.naturalOrder()))));
    }

    private static void sortVernstaltungsformListById(
            List<Veranstaltungsform> veranstaltungsformen) {
        veranstaltungsformen.sort(Comparator.nullsLast(Comparator.comparing(Veranstaltungsform::getId, Comparator.nullsLast(Comparator.naturalOrder()))));
    }

    private static void  sortZusatzfeldListById(List<Zusatzfeld> zusatzfelder) {
        zusatzfelder.sort(Comparator.nullsLast(Comparator.comparing(Zusatzfeld::getId, Comparator.nullsLast(Comparator.naturalOrder()))));
    }
}
