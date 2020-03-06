package mops.module.repositories;

import mops.module.database.Modul;
import org.springframework.data.repository.CrudRepository;

public interface ModulRepository extends CrudRepository<Modul, Long> {
    Antrag getCorrespondingAntrag(Modul modul);
}
