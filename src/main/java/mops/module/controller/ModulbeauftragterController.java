package mops.module.controller;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import javax.annotation.security.RolesAllowed;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;


@Controller
@SessionScope
@RequestMapping("/module")
public class ModulbeauftragterController {

    @GetMapping("/modulbeauftragter")
    @RolesAllowed({"ROLE_orga", "ROLE_sekretariat"})
    public String module(KeycloakAuthenticationToken token, Model model) {
        model.addAttribute("account", createAccountFromPrincipal(token));
        return "modulbeauftragter";
    }
}
