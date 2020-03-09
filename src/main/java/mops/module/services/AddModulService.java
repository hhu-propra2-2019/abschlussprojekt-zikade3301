package mops.module.services;

import java.util.UUID;
import mops.module.database.Modul;
import mops.module.repositories.AntragRepository;
import mops.module.repositories.ModulRepository;
import org.springframework.stereotype.Service;

@Service
public class AddModulService {
    AntragRepository antragRepo;
    ModulRepository modulRepo;

    UpdateModulService updateModulService;

    public AddModulService(AntragRepository antragRepo, ModulRepository modulRepo) {
        this.antragRepo = antragRepo;
        this.modulRepo = modulRepo;

        updateModulService = new UpdateModulService(antragRepo, modulRepo);
    }

    public boolean addModule(Modul modul) {
//        String modulID = UUID.randomUUID().toString();
//        Modul neues_modul = new Modul();


        return updateModulService.updateModul(modul);
    }

}
