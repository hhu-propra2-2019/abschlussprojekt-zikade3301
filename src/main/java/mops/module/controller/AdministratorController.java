package mops.module.controller;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import mops.module.services.AntragService;
import mops.module.services.JsonService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;

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
}

