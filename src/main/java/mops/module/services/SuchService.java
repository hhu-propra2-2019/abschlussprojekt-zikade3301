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

            PreparedStatement stmt = conn.prepareStatement("select * From Tabelle WHERE first like ? ");
            stmt.setString(1, "%" + searchinput + "%");//1 specifies the first parameter in the query i.e. name

            //ZUR NOT NEUE TABLE ZU IMPLEMENTIERUNGSZWECKEN
//            String sql = "DROP TABLE IF EXISTS Tabelle";
//
//            stmt.executeUpdate(sql);
//
//            String sql1 = "CREATE TABLE Tabelle " +
//                    "(id INTEGER NOT NULL, " +
//                    " first VARCHAR(255), " +
//                    " PRIMARY KEY ( id ))";
//            stmt.executeUpdate(sql1);
//            String sql2 = "INSERT INTO Tabelle (id, first) VALUES (1, 'MICHA')";
//            stmt.executeUpdate(sql2);
//            String sql3 = "INSERT INTO Tabelle (id, first) VALUES (2, 'Roman')";
//            String sql4 = "INSERT INTO Tabelle (id, first) VALUES (3, 'Ein ICE fährt schnell')";
//            stmt.executeUpdate(sql4);
//            String sql5 = "INSERT INTO Tabelle (id, first) VALUES (4, 'IVEN searchinput')";
//            stmt.executeUpdate(sql5);

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
