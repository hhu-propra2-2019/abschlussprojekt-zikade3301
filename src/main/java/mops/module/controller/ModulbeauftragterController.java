package mops.module.controller;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import javax.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import mops.module.database.Modulkategorie;
import mops.module.services.ModulService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;


@Controller
@SessionScope
@RequiredArgsConstructor
@RequestMapping("/module")
public class ModulbeauftragterController {

    /**
     * Get-Mapping für die "Module bearbeiten"-Seite für die Organisatoren und das Sekretariat.
     * @param model Model für die HTML-Datei.
     * @param token Keycloak-Token.
     * @return View für die "Module bearbeiten"-Seite.
     */
    @GetMapping("/modulbeauftragter")
    @RolesAllowed({"ROLE_orga", "ROLE_sekretariat"})
    public String module(KeycloakAuthenticationToken token, Model model) {
        model.addAttribute("account", createAccountFromPrincipal(token));
        return "modulbeauftragter";
    }
}
