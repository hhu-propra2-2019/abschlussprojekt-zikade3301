package mops.module;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import java.sql.SQLException;
import java.util.List;
import mops.module.database.Modul;
import mops.module.services.SuchService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

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
        List<Modul> searchResults = suchService.searchForModuleByTitle(searchField);
        model.addAttribute("searchResults", searchResults);
        //TODO: new request for modules only including the testresults
        return "searchresults";
    }

}