package mops.module.controller;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import mops.module.database.Modul;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsform;
import mops.module.database.Zusatzfeld;

@Data
@AllArgsConstructor
public class ModulWrapper {

    Modul modul;
    List<Veranstaltung> veranstaltungen;
    List<Veranstaltungsform> [] veranstaltungsformen;
    List<Zusatzfeld> [] zusatzfelder;

    @SuppressWarnings("uncheccked")
    public void initEmpty(int veranstaltungsanzahl, int veranstaltungsformenProVeranstaltung,
                          int zusatzfelderProVeranstaltung) {
        veranstaltungen = new LinkedList<>();
        veranstaltungsformen = new LinkedList[veranstaltungsanzahl];
        zusatzfelder = new LinkedList[veranstaltungsanzahl];

        for (int i = 0; i < veranstaltungsanzahl; i++) {
            veranstaltungsformen[i] = new LinkedList<>();
            zusatzfelder[i] = new LinkedList<>();
            Veranstaltung veranstaltung = new Veranstaltung();
            veranstaltungen.add(veranstaltung);
            for (int j = 0; j < veranstaltungsformenProVeranstaltung; j++) {
                Veranstaltungsform vf = new Veranstaltungsform();
//                vf.setVeranstaltung(veranstaltungen.get(i));
                veranstaltungsformen[i].add(vf);
            }
            for (int j = 0; j < zusatzfelderProVeranstaltung; j++) {
                Zusatzfeld zf = new Zusatzfeld();
//                zf.setVeranstaltung(veranstaltungen.get(i));
                zusatzfelder[i].add(zf);
            }
        }
    }

    public void initPrefilled(int veranstaltungsformenProVeranstaltung,
                              int zusatzfelderProVeranstaltung) {
        veranstaltungen = new LinkedList<>(modul.getVeranstaltungen());
        veranstaltungsformen = new LinkedList[veranstaltungen.size()];
        zusatzfelder = new LinkedList[veranstaltungen.size()];
        for (int i = 0; i < veranstaltungen.size(); i++) {
            veranstaltungsformen[i] = new LinkedList<>(veranstaltungen.get(i).getVeranstaltungsformen());
            zusatzfelder[i] = new LinkedList<>(veranstaltungen.get(i).getZusatzfelder());
            while (veranstaltungsformen[i].size() < veranstaltungsformenProVeranstaltung) {
                Veranstaltungsform vf = new Veranstaltungsform();
//                vf.setVeranstaltung(veranstaltungen.get(i));
                veranstaltungsformen[i].add(vf);
            }
            while (zusatzfelder[i].size() < zusatzfelderProVeranstaltung) {
                Zusatzfeld z = new Zusatzfeld();
//                z.setVeranstaltung(veranstaltungen.get(i));
                zusatzfelder[i].add(z);
            }

//
//            veranstaltungsformen[i] = new LinkedList<>(veranstaltungen.get(i).getVeranstaltungsformen());
//            while(veranstaltungsformen[i].size() < veranstaltungsformenProVeranstaltung) {
////            for (int j = 0; j < veranstaltungsformenProVeranstaltung - veranstaltungsformen[i].size(); j++) {
//                Veranstaltungsform vf = new Veranstaltungsform();
//                vf.setVeranstaltung(veranstaltungen.get(i));
//                veranstaltungsformen[i].add(vf);
//            }
//        }
//        for (int i = 0; i < veranstaltungen.size(); i++) {
//            zusatzfelder[i] = new LinkedList<>(veranstaltungen.get(i).getZusatzfelder());
//            while(zusatzfelder[i].size() < zusatzfelderProVeranstaltung) {
////            for (int j = 0; j < zusatzfelderProVeranstaltung - zusatzfelder[i].size(); j++) {
//                Zusatzfeld z = new Zusatzfeld();
//                z.setVeranstaltung(veranstaltungen.get(i));
//                zusatzfelder[i].add(z);
//            }
        }
    }
}
