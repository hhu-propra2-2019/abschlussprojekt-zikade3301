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
    List<Veranstaltungsform> veranstaltungsformen;
    List<Zusatzfeld> zusatzfelder;

    public void initEmpty(int veranstaltungsanzahl, int veranstaltungsformenProVeranstaltung,
                          int zusatzfelderProVeranstaltung) {
        veranstaltungen = new LinkedList<>();
        veranstaltungsformen = new LinkedList<>();
        zusatzfelder = new LinkedList<>();
        for (int i = 0; i < veranstaltungsanzahl; i++) {
            Veranstaltung veranstaltung = new Veranstaltung();
            veranstaltungen.add(veranstaltung);
            for (int j = 0; j < veranstaltungsformenProVeranstaltung; j++) {
                Veranstaltungsform vf = new Veranstaltungsform();
                vf.setVeranstaltung(veranstaltung);
                veranstaltungsformen.add(vf);
            }
            for (int j = 0; j < zusatzfelderProVeranstaltung; j++) {
                Zusatzfeld zf = new Zusatzfeld();
                zf.setVeranstaltung(veranstaltung);
                zusatzfelder.add(zf);
            }
        }
    }

    public void initPrefilled(int veranstaltungsformenProVeranstaltung, int zusatzfelderProVeranstaltung) {
        veranstaltungen = new LinkedList<>(modul.getVeranstaltungen());
        veranstaltungsformen = new LinkedList<>();
        zusatzfelder = new LinkedList<>();
        for (Veranstaltung v : veranstaltungen) {
            veranstaltungsformen.addAll(v.getVeranstaltungsformen());
            for (int i = 0; i < veranstaltungsformenProVeranstaltung - v.getVeranstaltungsformen().size(); i++) {
                Veranstaltungsform vf = new Veranstaltungsform();
                vf.setVeranstaltung(v);
                veranstaltungsformen.add(vf);
            }
        }
        for (Veranstaltung v : veranstaltungen) {
            zusatzfelder.addAll(v.getZusatzfelder());
            for (int i = 0; i < zusatzfelderProVeranstaltung - v.getZusatzfelder().size(); i++) {
                Zusatzfeld zf = new Zusatzfeld();
                zf.setVeranstaltung(v);
                zusatzfelder.add(zf);
            }
        }
    }
}
