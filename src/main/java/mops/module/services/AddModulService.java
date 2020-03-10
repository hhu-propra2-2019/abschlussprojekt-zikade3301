package mops.module.services;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.repositories.AntragsRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AddModulService {
    private final AntragsRepository antragsRepository;
    private final JSONService jsonService;

    /**
     * Erstellt aus einem Modul ein Antrag
     *
     * @param modul
     * @return
     */
    Antrag toAntrag(Modul modul, LocalDateTime localDateTime) {
        String jsonObject = jsonService.modulToJSONObject(modul);
        Antrag antrag = new Antrag();
        antrag.setModul(jsonObject);
        return antrag;

    }

}
