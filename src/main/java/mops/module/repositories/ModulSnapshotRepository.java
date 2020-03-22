package mops.module.repositories;

import java.util.List;
import mops.module.database.Modul;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ModulSnapshotRepository extends CrudRepository<Modul, Long> {

    List<Modul> findAll();

    @Query("SELECT m FROM Modul m JOIN m.veranstaltungen v JOIN v.semester s WHERE s = :semester")
    List<Modul> findModuleBySemester(@Param("semester") String semester);

    /*
    @Query(value = "SELECT * FROM Modul WHERE to_tsvector('german', titel_Deutsch, titel_Englisch) @@ to_tsquery('german', searchinput)", nativeQuery = true)
    List<Modul> findModuleByTitle(String searchinput);
    */

    @Query("SELECT m FROM Modul m WHERE LOWER(titel_Deutsch) LIKE %?1% OR LOWER(titel_Englisch) LIKE %?1%")
    List<Modul> findModuleByTitle(String searchinput);

    @Query("SELECT m, v, b FROM Modul m JOIN m.veranstaltungen v JOIN v.beschreibung b " +
        "WHERE LOWER(titel_Deutsch) LIKE %?1% OR " +
        "LOWER(titel_Englisch) LIKE %?1% OR " +
        "LOWER(leistungspunkte) LIKE %?1% OR " +
        "LOWER(inhalte) LIKE %?1% OR " +
        "LOWER(lernergebnisse) LIKE %?1%")
    List<Modul> fullTextSearchForModule(String searchinput);

    /*
    @Query(value = "select m.* from Modul m join veranstaltung v on m.id = v.modul_id where to_tsvector('german', inhalte) @@ plainto_tsquery('german', ?1)", nativeQuery = true)
    List<Modul> fullTextSearchForModule(String searchinput);
    */

}

