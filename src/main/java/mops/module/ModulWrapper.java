package mops.module;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import mops.module.database.Modul;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsform;

@Data
@AllArgsConstructor
public class ModulWrapper {

    Modul modul;

    List<Veranstaltung> veranstaltungen;

    List<Veranstaltungsform> [] veranstaltungsformen;

}