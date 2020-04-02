package mops.module.controller;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import java.time.format.DateTimeFormatter;
import javax.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import mops.module.database.Modul;
import mops.module.services.AntragService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/module")
public class AdministratorController {

    private final AntragService antragService;


    /**Administrator Controller.
     *
     * @param token Der Token von keycloak für die Berechtigung.
     * @param model Modell für die HTML Datei.
     * @return View administrator
     */
    @GetMapping("/administrator")
    @Secured("ROLE_sekretariat")
    public String administrator(KeycloakAuthenticationToken token, Model model) {

        model.addAttribute("formatter", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        model.addAttribute("account", createAccountFromPrincipal(token));
        model.addAttribute("allAntraege", antragService.getAlleOffenenAntraegeGeordnetDatum());

        return "administrator";
    }

    /**
     * Get-Mapping für das Anzeigen einer Version für ein Modul.
     * @param modulId ID des Moduls
     * @param version Version
     * @param model Modell für die HTML-Datei.
     * @param token Der Token von keycloak für die Berechtigung.
     * @return View für die Modulerstellung.
     */
    @GetMapping("/modulversion")
    @RolesAllowed({"ROLE_sekretariat"})
    public String showModulVersion(
            @RequestParam(name = "modul") Long modulId,
            @RequestParam(name = "version") int version,
            Model model,
            KeycloakAuthenticationToken token) {

        Modul modul = antragService.getAllVersionsOfModulOldestFirst(modulId).get(version);
        model.addAttribute("modul", modul);
        model.addAttribute("account", createAccountFromPrincipal(token));

        return "moduldetails";
    }



    @PostMapping("/modulversion")
    @Secured("ROLE_sekretariat")
    public String deleteAntrag(
            @RequestParam(name = "modul") Long modulId,
            KeycloakAuthenticationToken token,
            Model model) {



        model.addAttribute("formatter", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        model.addAttribute("account", createAccountFromPrincipal(token));
        model.addAttribute("allAntraege", antragService.getAlleOffenenAntraegeGeordnetDatum());

        return "administrator";
    }

}

