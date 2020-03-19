package mops.module.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mops.module.database.Modul;
import mops.module.repositories.ModulSnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuchService {

    @Autowired
    ModulSnapshotRepository modulSnapshotRepository;

    /**
     * Search for module.
     *
     * @param searchinput the given string to search
     */
    public List<Modul> searchForModuleByTitle(String searchinput, Connection conn) {
        List<Modul> results = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id FROM Modul WHERE LOWER(titel_Deutsch) LIKE ?  OR LOWER(titel_Englisch) Like ?"
            );
            stmt.setString(1, "%" + searchinput.toLowerCase() + "%");
            stmt.setString(2, "%" + searchinput.toLowerCase() + "%");

            ResultSet searchResult = stmt.executeQuery();

            while (searchResult.next()) {
                Long id = searchResult.getLong("id");
                Modul modul = modulSnapshotRepository.findById(id).orElse(null);
                results.add(modul);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Uses Postgres internal FullTextSearch to get results in module description
     *
     * @param searchinput given searchterm from user
     * @param conn        connection to database
     * @return list of modules where searchterm was found in description
     */
    public List<Modul> searchInVeranstaltungsbeschreibung(String searchinput, Connection conn) {
        List<Modul> results = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT modul_id FROM veranstaltung WHERE to_tsquery('german', inhalte) @@ to_tsvector('german', ?)"
            );
            stmt.setString(1, searchinput);

            ResultSet searchResult = stmt.executeQuery();

            while (searchResult.next()) {
                Long id = searchResult.getLong("modul_id");
                Modul modul = modulSnapshotRepository.findById(id).orElse(null);
                results.add(modul);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
