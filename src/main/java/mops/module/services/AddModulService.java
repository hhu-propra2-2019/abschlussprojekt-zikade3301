package mops.module.services;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.database.Modulbeauftragter;
import mops.module.repositories.AntragsRepository;
import mops.module.repositories.AntragRepository;
import mops.module.repositories.ModulRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AddModulService {
    private final AntragsRepository antragsRepository;


    /**
     * Erstellt aus einem Modul ein Antrag
     *
     * @param modul
     * @return
     */
    Antrag toAntrag(Modul modul, LocalDateTime localDateTime) {
        String jsonObject=JSONService.ModultoJSONOBject(modul);
        Antrag antrag=new Antrag();
        antrag.setModul(jsonObject);
        return antrag;

    }

}
