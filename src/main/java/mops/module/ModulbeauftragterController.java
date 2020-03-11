package mops.module;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/module")
public class ModulbeauftragterController {

    @GetMapping("/modulbeauftragter")
    @Secured("ROLE_orga")
    public String module(KeycloakAuthenticationToken token, Model model) {
        model.addAttribute("account", createAccountFromPrincipal(token));
        return "modulbeauftragter";
    }
}