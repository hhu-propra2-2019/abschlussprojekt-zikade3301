package mops.module.controller;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import mops.module.services.ModulService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/module")
public class SemesterTagController {


    private ModulService modulService;

    public SemesterTagController(ModulService modulService) {
        this.modulService = modulService;
    }

    /**
     * Controller, der das Request für die Erstellung eines SemesterTags entgegennimmt.
     *
     * @param semesterTag     Der SemesterTag, der der Veranstaltung hinzugefügt werden soll
     * @param idVeranstaltung ID der Veranstaltung, die das Tag erhalten soll
     * @param idModul         ID des Moduls, das die Veranstaltung beinhaltet
     * @param model           Model für die HTML-Datei.
     * @param token           Der Token von keycloak für die Berechtigung.
     * @return View Modulbeauftragter
     */
    @PostMapping("/semesterTag/create")
    @Secured("ROLE_sekretariat")
    public String addSemesterTagToVeranstaltung(
            @RequestParam(name = "inputTag", required = true) String semesterTag,
            @RequestParam(name = "idVeranstaltung") String idVeranstaltung,
            @RequestParam(name = "idModul") String idModul,
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

    /**
     * Controller, der den Request für das Löschen eines SemesterTags entgegennimmt.
     *
     * @param tagToDelete              Der SemesterTag, der gelöscht werden soll
     * @param idVeranstaltungTagDelete ID der Veranstaltung, die das Tag beinhaltet
     * @param idModulTagDelete          ID des Moduls, das die Veranstaltung beinhaltet
     * @param model                    Model für die HTML-Datei.
     * @param token                    Der Token von keycloak für die Berechtigung.
     * @return View Modulbeauftragter
     */
    @PostMapping("/semesterTag/delete")
    @Secured("ROLE_sekretariat")
    public String removeSemesterTagToVeranstaltung(
            @RequestParam(name = "tagToDelete", required = true) String tagToDelete,
            @RequestParam(name = "idVeranstaltungTagDelete") String idVeranstaltungTagDelete,
            @RequestParam(name = "idModulTagDelete") String idModulTagDelete,
            Model model,
            KeycloakAuthenticationToken token) {

        model.addAttribute("account", createAccountFromPrincipal(token));

        modulService.deleteTagVeranstaltungSemester(
                tagToDelete,
                Long.parseLong(idVeranstaltungTagDelete),
                Long.parseLong(idModulTagDelete)
        );
        return "redirect:/module/modulbeauftragter";
    }
}
