package mops.module;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

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
        //TODO: request for modules to display
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

    /**
     * Search method string.
     *
     * @param searchField the given input for searching.
     * @return the string index to stay on the page.
     */
    @GetMapping("/search")
    public String searchMethod(@RequestParam String searchField) {
        suchService.searchForModule(searchField);
        //TODO: new request for modules only including the searchresults
        return "index";
    }
}