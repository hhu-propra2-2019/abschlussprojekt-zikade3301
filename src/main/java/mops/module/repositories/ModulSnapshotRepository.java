package mops.module.repositories;

import java.util.List;
import mops.module.database.Modul;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ModulSnapshotRepository extends CrudRepository<Modul, Long> {
    @Query("SELECT m FROM Modul m JOIN m.veranstaltungen v JOIN v.semester s WHERE s = :semester")
    List<Modul> findModuleBySemester(@Param("semester") String semester);

    @Query("SELECT m FROM Modul m WHERE LOWER(titel_Deutsch) LIKE %?1% OR LOWER(titel_Englisch) LIKE %?1%")
    List<Modul> findModuleByTitle(String searchinput);

}