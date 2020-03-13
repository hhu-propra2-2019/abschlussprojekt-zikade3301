package mops.module;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


@Controller
@SessionScope
@RequestMapping("/module")
public class IndexController {

    /**
     * Index string.
     *
     * @param token the token of keycloak for permissions.
     * @param model the model of keycloak for permissions.
     * @return the string "index" which is the unsecured page for every user.
     */
    @GetMapping("/")
    public String index(KeycloakAuthenticationToken token, Model model) {
        if (token != null) {
            model.addAttribute("account",createAccountFromPrincipal(token));
        }
        return "index";
    }

    @GetMapping("/search")
    public String searchMethodTmp(@RequestParam String searchField) {
        searchForModule(searchField);
        return "index";
    }


    // "Tabelle" durch Tabellennamen ersetzen
    public void searchForModule(String searchinput) {
        try {
            String url = "jdbc:postgresql://localhost:3301/Modulhandbuch";
            Connection conn = DriverManager.getConnection(url,"root","zikade3301");
            Statement stmt = conn.createStatement();
            ResultSet searchResult;

            // ZUR NOT NEUE TABLE ZU IMPLEMENTIERUNGSZWECKEN
//            String sql1 = "CREATE TABLE Tabelle " +
//                    "(id INTEGER not NULL, " +
//                    " first VARCHAR(255), " +
//                    " PRIMARY KEY ( id ))";
//            stmt.executeUpdate(sql1);
//            String sql2 = "INSERT INTO Tabelle (id, first) VALUES (1, 'MICHA')";
//            stmt.executeUpdate(sql2);
//            String sql3 = "INSERT INTO Tabelle (id, first) VALUES (2, 'Roman')";
//            stmt.executeUpdate(sql3);
//            String sql4 = "INSERT INTO Tabelle (id, first) VALUES (3, 'Ein ICE fährt schnell')";
//            stmt.executeUpdate(sql4);
//            String sql5 = "INSERT INTO Tabelle (id, first) VALUES (4, 'IVEN')";
//            stmt.executeUpdate(sql5);

            // TODO KEIN SQL INJECTION
            searchResult = stmt.executeQuery("select * From Tabelle WHERE first like '%"+searchinput+"%'");

            while ( searchResult.next() ) {
                String inhalt = searchResult.getString(2);
                System.out.println(inhalt + " enthält " + searchinput);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Moduldetails string.
     *
     * @param modulId the modul id
     * @param token   the token of keycloak for permissions.
     * @param model   the model of keycloak for permissions.
     * @return the string "moduldetails" for the selected module.
     */
    @RequestMapping("/moduldetails")
    public String moduldetails(
            @RequestParam("modulId") String modulId,
            KeycloakAuthenticationToken token,
            Model model) {
        if (token != null) {
            model.addAttribute("account",createAccountFromPrincipal(token));
        }
        model.addAttribute("modulId",modulId);
        return "moduldetails";
    }

}