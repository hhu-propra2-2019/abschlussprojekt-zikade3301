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
public class ModulVisibilityController {


    @Autowired
    private ModulService modulService;


    /**
     * Controller, der den Request für das Ändern des Sichtbarkeitsstatus'
     * eines Moduls entgegennimmt.
     *
     * @param modulToChange Id des Moduls, dessen Sichtbarkeitsstatus geändert wetden soll
     * @param model         Model für die HTML-Datei.
     * @param token         Der Token von keycloak für die Berechtigung.
     * @return View Modulbeauftragter
     */
    @PostMapping("/setModulVisibility")
    @Secured("ROLE_sekretariat")
    public String changeVisibilityOfModules(
            @RequestParam(name = "modulToChange") String modulToChange,
            Model model,
            KeycloakAuthenticationToken token) {

        System.out.println(modulToChange);

        modulService.changeVisibility(
                Long.parseLong(modulToChange)
        );

        model.addAttribute("account", createAccountFromPrincipal(token));


        return "redirect:/module/modulbeauftragter";
    }
}
