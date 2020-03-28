package mops.module.controller;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mops.module.database.Modul;
import mops.module.services.SuchService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

@Controller
@RequiredArgsConstructor
@SessionScope
@RequestMapping("/module")
public class SearchController {

    private final SuchService suchService;

    /**
     * Searchresults string.
     *
     * @param searchField Benutzer Eingabe für das Such Formular
     * @param token       Token von Keycloak der den Zugriff auf eine Seite regelt
     * @param model       Model für die HTML-Datei.
     * @return            View Searchresults
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
