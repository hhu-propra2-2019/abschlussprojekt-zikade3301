package mops.module.repositories;

import java.util.List;
import mops.module.database.Modul;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModulSnapshotRepository extends CrudRepository<Modul, Long> {

    @Query("SELECT m FROM Modul m WHERE LOWER(titelDeutsch) LIKE %?1% OR LOWER(titelEnglisch) LIKE %?1%")
    List<Modul> findModuleByTitle(String searchinput);

}
