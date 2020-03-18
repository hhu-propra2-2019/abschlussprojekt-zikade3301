package mops.module;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import lombok.RequiredArgsConstructor;
import mops.module.services.AntragService;
import mops.module.services.ModulService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.annotation.SessionScope;



@Controller
@SessionScope
@RequiredArgsConstructor
@RequestMapping("/module")
public class IndexController {

    private final ModulService modulService;
    private final AntragService antragService;


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
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        model.addAttribute("allModules", modulService.getAllModule());
        return "index";
    }

    /**
     * Moduldetails string.
     *
     * @param id the modul id
     * @param token   the token of keycloak for permissions.
     * @param model   the model of keycloak for permissions.
     * @return the string "moduldetails" for the selected module.
     */
    @RequestMapping(value = "/moduldetails/{id}", method = RequestMethod.GET)
    public String moduldetails(
            @PathVariable String id,
            KeycloakAuthenticationToken token,
            Model model) {
        if (token != null) {
            model.addAttribute("account",createAccountFromPrincipal(token));
        }
        //TODO: getModulById verwenden
        //model.addAttribute("modul", modulService.getAllModule());
        return "moduldetails";
    }

}