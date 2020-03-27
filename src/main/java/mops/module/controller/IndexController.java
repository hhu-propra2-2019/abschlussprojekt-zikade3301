package mops.module.controller;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import mops.module.database.Modulkategorie;
import mops.module.services.AntragService;
import mops.module.services.ModulService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.annotation.SessionScope;



@Controller
@SessionScope
@RequiredArgsConstructor
@RequestMapping("/module")
public class IndexController {

    private final ModulService modulService;
    private final AntragService antragService;

    private static final int NUMBER_OF_PAST_SEMESTERS = 1;
    private static final int NUMBER_OF_NEXT_SEMESTERS = 4;

    /**
     * Index string.
     *
     * @param token Der Token von keycloak für die Berechtigung.
     * @param model Model für die HTML-Datei.
     * @return View Index
     */
    @GetMapping("/")
    public String index(KeycloakAuthenticationToken token, Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        model.addAttribute("allModules", modulService.getAllModule());
        model.addAttribute("allCategories", Modulkategorie.values());
        model.addAttribute("nextSemesters",
                ModulService.getPastAndNextSemesters(LocalDateTime.now(),
                        NUMBER_OF_PAST_SEMESTERS, NUMBER_OF_NEXT_SEMESTERS));
        return "index";
    }

    /**
     * Moduldetails string.
     *
     * @param id the modul id
     * @param token   the token of keycloak for permissions.
     * @param model   the model of keycloak for permissions.
     * @return the string "moduldetails" for the selected module.
     */
    @RequestMapping(value = "/moduldetails/{id}", method = RequestMethod.GET)
    public String moduldetails(
            @PathVariable String id,
            KeycloakAuthenticationToken token,
            Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        model.addAttribute("modul", modulService.getModulById(Long.parseLong(id)));
        return "moduldetails";
    }

    /**
     * Semesteransicht string.
     *
     * @param semester the corresponding semester
     * @param token   the token of keycloak for permissions.
     * @param model   the model of keycloak for permissions.
     * @return the string "index" with modules in the selected semester.
     */
    @RequestMapping(value = "/semester/{semester}", method = RequestMethod.GET)
    public String semesterAnsicht(
            @PathVariable String semester,
            KeycloakAuthenticationToken token,
            Model model) {
        if (token != null) {
            model.addAttribute("account", createAccountFromPrincipal(token));
        }
        model.addAttribute("allModules", modulService.getModuleBySemester(semester));
        model.addAttribute("allCategories", Modulkategorie.values());
        model.addAttribute("nextSemesters",
                ModulService.getPastAndNextSemesters(LocalDateTime.now(),
                        NUMBER_OF_PAST_SEMESTERS, NUMBER_OF_NEXT_SEMESTERS));
        return "index";
    }

}