package mops.module;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import mops.module.database.Modul;
import mops.module.database.Veranstaltung;

@Data
@AllArgsConstructor
public class ModulWrapper {

    Modul modul;

    List<Veranstaltung> veranstaltungen;

}
