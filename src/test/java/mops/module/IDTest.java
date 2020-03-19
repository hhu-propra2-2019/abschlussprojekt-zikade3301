package mops.module;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import mops.module.database.Modul;
import mops.module.repositories.ModulSnapshotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IDTest {

    @Autowired
    ModulSnapshotRepository modulSnapshotRepository;

    @Test
    void newModulGetsAutoIdByDatabase() {
        modulSnapshotRepository.deleteAll();
        Modul modul = new Modul();
        modul.setTitelDeutsch("ID nicht manuell gesetzt");
        modulSnapshotRepository.save(modul);
        assertNotNull(modulSnapshotRepository.findAll().iterator().next().getId());
    }
}

