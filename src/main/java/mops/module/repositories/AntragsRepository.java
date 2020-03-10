package mops.module.repositories;

import mops.module.database.Antrag;
import org.springframework.data.repository.CrudRepository;

public interface AntragsRepository extends CrudRepository<Antrag, Long> {
}
