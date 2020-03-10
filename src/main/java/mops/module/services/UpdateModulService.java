package mops.module.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import mops.module.database.Modul;
import mops.module.database.Veranstaltung;
import mops.module.repositories.AntragsRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateModulService {

    private final AntragsRepository antragsRepository;

    public UpdateModulService(AntragsRepository antragsRepository) {
        this.antragsRepository = antragsRepository;
    }
}
