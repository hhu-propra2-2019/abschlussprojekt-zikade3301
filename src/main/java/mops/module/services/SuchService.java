package mops.module.services;

import java.util.List;
import mops.module.database.Modul;
import mops.module.repositories.ModulSnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuchService {

    @Autowired
    ModulSnapshotRepository modulSnapshotRepository;

    /**
     * Search for module.
     *
     * @param searchinput the given string to search
     */
    public List<Modul> searchForModuleByTitle(String searchinput) {
        return modulSnapshotRepository.findModuleByTitle(searchinput.toLowerCase());
    }

    /**
     * Uses Postgres internal FullTextSearch to get results in module description
     *
     * @param searchinput given searchterm from user
     * @return list of modules where searchterm was found in description
     */
    public List<Modul> searchInVeranstaltungsbeschreibung(String searchinput) {
        return modulSnapshotRepository.fullTextSearchForModule(searchinput);
    }
}
