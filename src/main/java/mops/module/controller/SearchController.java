package mops.module.controller;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

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

    /*
    @GetMapping("/searchresults")
    public String searchresults(KeycloakAuthenticationToken token, Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        return "searchresults";
    }
    */

    /**
     * Searchresults string.
     *
     * @param searchField User input in search form on index
     * @param token       the token of keycloak for permissions
     * @param model       the model of keycloak for permissions
     * @return the string "searchresults" for the carried out search
     */
    @GetMapping("/search")
    public String search(
            @RequestParam String searchField,
            KeycloakAuthenticationToken token,
            Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }

        List<Modul> searchResults = suchService.search(searchField);
        model.addAttribute("searchResults", searchResults);
        return "searchresults";
    }
}
