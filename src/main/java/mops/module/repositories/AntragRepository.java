package mops.module.repositories;

import java.util.List;
import mops.module.database.Antrag;
import org.springframework.data.repository.CrudRepository;

public interface AntragRepository extends CrudRepository<Antrag, Long> {
    List<Antrag> findByModulId(Long modulid);

    void deleteById(Long id);
}
