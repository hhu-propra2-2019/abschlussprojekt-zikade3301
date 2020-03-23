package mops.module.controller;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import java.util.Map;
import javax.annotation.security.RolesAllowed;
import mops.module.database.Modul;
import mops.module.services.AntragService;
import mops.module.services.FormService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;


@Controller
@SessionScope
@RequestMapping("/module")
public class ModulerstellungController {


    private AntragService antragService;

    @Autowired
    public ModulerstellungController(AntragService antragService) {
        this.antragService = antragService;
    }

    /**
     * Controller für das Generieren eines Modulerstellungsformulars für die eingegebene Anzahl
     * von Veranstaltungen.
     * @param veranstaltungsanzahl Anzahl der Veranstaltungen.
     * @param model Modell für die HTML-Datei.
     * @param token Keycloak-Token.
     * @return View für die Modulerstellung.
     */
    @GetMapping("/modulerstellung")
    @RolesAllowed({"ROLE_orga", "ROLE_sekretariat"})
    public String result(
            @RequestParam(name = "veranstaltungsanzahl", required = true) int veranstaltungsanzahl,
            Model model,
            KeycloakAuthenticationToken token) {
        model.addAttribute("account", createAccountFromPrincipal(token));
        if (veranstaltungsanzahl < 1) {
            model.addAttribute("veranstaltungsanzahl", 1);
        } else {
            model.addAttribute("veranstaltungsanzahl", veranstaltungsanzahl);
        }
        return "modulerstellung";
    }

    /**
     * Controller für das Entgegebennehmen eines Post-Requests mit den Formulardaten für die
     * Erstellung eines Modulantrags.
     * @param allParams Alle Parameter des Modulformulars als Map, wobei jeweils der im Formular
     *                  übergebene name der Key und die Nutzereingabe als String der Value ist.
     * @param model Model für die HTML-Datei.
     * @param token Keycloak-Token.
     * @return View für die Modulerstellung.
     */
    @PostMapping("/modulerstellung")
    @RolesAllowed({"ROLE_orga", "ROLE_sekretariat"})
    public String addCreationAntrag(@RequestParam Map<String,String> allParams,
                                    Model model,
                                    KeycloakAuthenticationToken token) {

        Modul modul = FormService.readModulFromParameterMap(allParams);
        String antragsteller = ((KeycloakPrincipal)token.getPrincipal()).getName();

        antragService.addModulCreationAntrag(modul, antragsteller);
        return "modulbeauftragter";
    }
}
