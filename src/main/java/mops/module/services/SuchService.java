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
                    "SELECT * FROM Modul WHERE titel_Deutsch LIKE ?  OR titel_Englisch Like ?"
            );
            stmt.setString(1, "%" + searchinput + "%");
            stmt.setString(2, "%" + searchinput + "%");

            ResultSet searchResult = stmt.executeQuery();

            while (searchResult.next()) {
                Long id = searchResult.getLong("id");
                String titelDeutsch = searchResult.getString("titelDeutsch");
                String titelEnglisch = searchResult.getString("titelEnglisch");
                Modul modul = new Modul(id, titelDeutsch, titelEnglisch, null, null, null, null, null, true, null, null);
                result.add(modul);
            }
        } catch (SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return result;
    }

}