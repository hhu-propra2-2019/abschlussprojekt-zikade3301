package mops.module;

import static mops.module.KeycloakAccount.createAccountFromPrincipal;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
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

}