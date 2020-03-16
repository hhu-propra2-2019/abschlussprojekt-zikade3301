package mops.module;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import java.sql.Connection;
import java.sql.DriverManager;
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
public class IndexController {

    @Autowired
    private SuchService suchService;

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

    @GetMapping("/search")
    public String searchMethodTmp(@RequestParam String searchField, Model model) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:3301/Modulhandbuch", "root", "zikade3301");
        List<Modul> searchResults = suchService.searchForModuleByTitle(searchField, conn);
        model.addAttribute("searchResults", searchResults);
        //TODO: new request for modules only including the testresults
        return "index";
    }
}