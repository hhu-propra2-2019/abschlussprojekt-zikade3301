package mops.module;

import java.util.List;
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

    List<Zusatzfeld> [] zusatzfeld;


}