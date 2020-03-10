package mops.module.services;

import lombok.RequiredArgsConstructor;
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
     * Erstellt aus einem ModulDTO ein Modul Objekt
     *
     * @param modul
     * @return
     */

    Modul toAendeung(Modul modul) {
        List<Modulbeauftragter> modulbeauftragte =
                modulDTO.modulbeuftragteDTO.stream()
                        .forEach(Modulbeauftragter::new);

        Modul modul = new Modul(modulDTO.titelDeutsch,
                modulDTO.titelEnglisch,
                modulbeauftragte,
                );

        );
    }

    public boolean addModule(Modul modul) {
//        String modulID = UUID.randomUUID().toString();
//        Modul neues_modul = new Modul();


        return updateModulService.updateModul(modul);
    }

}
