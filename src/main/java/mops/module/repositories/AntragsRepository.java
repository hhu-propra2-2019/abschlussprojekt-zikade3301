package mops.module.repositories;

import java.util.List;
import mops.module.database.Antrag;
import org.springframework.data.repository.CrudRepository;

public interface AntragsRepository extends CrudRepository<Antrag, Long> {
    List<Antrag> findByModulid(Long modulid);
}
