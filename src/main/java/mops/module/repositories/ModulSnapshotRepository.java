package mops.module.repositories;

import java.util.List;
import mops.module.database.Modul;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ModulSnapshotRepository extends CrudRepository<Modul, Long> {
    @Query(value = "SELECT DISTINCT m.* FROM modul m JOIN veranstaltung v ON m.id = v.modul_id " +
            "JOIN veranstaltung_semester vs ON vs.veranstaltung_id = v.id " +
            "WHERE vs.semester = :sm", nativeQuery = true)
    List<Modul> findModuleBySemester(@Param("sm") String semester);
}