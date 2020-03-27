package mops.module.controller;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import mops.module.services.ModulService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/module")
public class SemesterTagController {


    @Autowired
    private ModulService modulService;

    /**
     * Add modul creation antrag string.
     *
     * @param semesterTag     Der SemesterTag, der der Veranstaltung hinzugefügt werden soll
     * @param idVeranstaltung ID der Veranstaltung, die das Tag erhalten soll
     * @param idModul         ID des Moduls, das die Veranstaltung beinhaltet
     * @param model           Model für die HTML-Datei.
     * @param token           Der Token von keycloak für die Berechtigung.
     * @return View Modulbeauftragter
     */
    @PostMapping("/semesterTag")
    @Secured("ROLE_sekretariat")
    public String addModulCreationAntrag(@RequestParam(name = "inputTag", required = true)
                                                 String semesterTag,
                                         @RequestParam(name = "idVeranstaltung")
                                                 String idVeranstaltung,
                                         @RequestParam(name = "idModul")
                                                 String idModul,
                                         Model model,
                                         KeycloakAuthenticationToken token) {

        model.addAttribute("account", createAccountFromPrincipal(token));

        modulService.tagVeranstaltungSemester(
                semesterTag,
                Long.parseLong(idVeranstaltung),
                Long.parseLong(idModul)
        );

        return "redirect:/module/modulbeauftragter";
    }
}
