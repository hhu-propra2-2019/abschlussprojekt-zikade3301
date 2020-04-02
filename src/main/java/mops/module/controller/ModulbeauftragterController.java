package mops.module.controller;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import java.time.LocalDateTime;
import java.util.List;
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

    private final ModulService modulService;

    /**
     * Get-Mapping für die "Module bearbeiten"-Seite für die Organisatoren und das Sekretariat.
     * @param model Model für die HTML-Datei.
     * @param token Der Token von keycloak für die Berechtigung.
     * @return View für die "Module bearbeiten"-Seite.
     */
    @GetMapping("/modulbeauftragter")
    @RolesAllowed({"ROLE_orga", "ROLE_sekretariat"})
    public String module(KeycloakAuthenticationToken token, Model model) {
        model.addAttribute("account", createAccountFromPrincipal(token));
        model.addAttribute("allCategories", Modulkategorie.values());
        model.addAttribute("allModules", modulService.getAllModule());
        model.addAttribute("allVisibleModules", modulService.getAllSichtbareModule());

        List<String> semesterWahl = ModulService.getPastAndNextSemesters(
                LocalDateTime.now(), 0, 4);
        model.addAttribute("allSemesters", semesterWahl);

        return "modulbeauftragter";
    }
}
