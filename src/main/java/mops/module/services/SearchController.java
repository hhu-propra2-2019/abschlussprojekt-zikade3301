package mops.module.services;

import mops.module.database.Modul;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

@Controller
@SessionScope
@RequestMapping("/module")
public class SearchController {

    @Autowired
    private SuchService suchService;

    @GetMapping("/searchresults")
    public String search(KeycloakAuthenticationToken token, Model model) {
        if (token != null) {
            model.addAttribute("account",createAccountFromPrincipal(token));
        }
        return "searchresults";
    }

    @GetMapping("/search")
    public String searchMethodTmp(@RequestParam String searchField, Model model) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:3301/Modulhandbuch", "root", "zikade3301");
        List<Modul> searchResults = suchService.searchForModuleByTitle(searchField, conn);
        model.addAttribute("searchResults", searchResults);
        //TODO: new request for modules only including the testresults
        return "searchresults";
    }

}
