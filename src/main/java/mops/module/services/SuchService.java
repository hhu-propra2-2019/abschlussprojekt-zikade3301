package mops.module.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.springframework.stereotype.Service;

@Service
public class SuchService {

    // "Tabelle" durch Tabellennamen ersetzen
    public void searchForModule(String searchinput) {
        try {
            String url = "jdbc:postgresql://localhost:3301/Modulhandbuch";
            Connection conn = DriverManager.getConnection(url, "root", "zikade3301");

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Tabelle WHERE first LIKE ? ");
            stmt.setString(1, "%" + searchinput + "%");

            ResultSet searchResult = stmt.executeQuery();

            while (searchResult.next()) {
                String inhalt = searchResult.getString(2);
                System.out.println(inhalt + " enthält " + searchinput);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

}
