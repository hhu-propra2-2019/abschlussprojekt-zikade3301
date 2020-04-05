package mops.module.controller;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.services.AntragService;
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

    private final AntragService antragService;
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
        model.addAttribute("formatter", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        model.addAttribute("account", createAccountFromPrincipal(token));
        model.addAttribute("allCategories", Modulkategorie.values());
        model.addAttribute("allModules", modulService.getAllModule());

        List<Modul> allSichtbareModule = modulService.getAllSichtbareModule();
        model.addAttribute("allVisibleModules", allSichtbareModule);

        ArrayList<LinkedList<Modul>> allVersions =
                antragService.getAllVersionsListFor(allSichtbareModule);
        model.addAttribute("allVersions", allVersions);
        ArrayList<LinkedList<Antrag>> allAntraege =
                antragService.getAllAntraegeListFor(allSichtbareModule);
        model.addAttribute("allAntraege", allAntraege);

        List<String> semesterWahl = ModulService.getPastAndNextSemestersForTagging();
        model.addAttribute("allSemesters", semesterWahl);

        return "modulbeauftragter";
    }
}
