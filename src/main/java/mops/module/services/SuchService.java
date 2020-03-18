package mops.module.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mops.module.database.Modul;
import org.springframework.stereotype.Service;

@Service
public class SuchService {

    /**
     * Search for module.
     *
     * @param searchinput the given string to search
     */
    // "Tabelle" durch Tabellennamen ersetzen
    public List<Modul> searchForModuleByTitle(String searchinput, Connection conn) throws SQLException {
        List<Modul> result = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM Modul WHERE LOWER(titel_Deutsch) LIKE ?  OR LOWER(titel_Englisch) Like ?"
            );
            stmt.setString(1, "%" + searchinput.toLowerCase() + "%");
            stmt.setString(2, "%" + searchinput.toLowerCase() + "%");

            ResultSet searchResult = stmt.executeQuery();

            while (searchResult.next()) {
                Long id = searchResult.getLong("id");
                String titelDeutsch = searchResult.getString("titel_Deutsch");
                String titelEnglisch = searchResult.getString("titel_Englisch");
                Modul modul = new Modul(id, titelDeutsch, titelEnglisch, null, null, null, null, null, true, null, null, null);
                result.add(modul);
            }
        } catch (SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return result;
    }

}
