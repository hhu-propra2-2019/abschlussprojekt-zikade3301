package mops.module.controller;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import lombok.RequiredArgsConstructor;
import mops.module.services.ModulService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@RequestMapping("/module")
public class ModulVisibilityController {

    private final ModulService modulService;


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

        modulService.changeVisibility(
                Long.parseLong(modulToChange)
        );

        model.addAttribute("account", createAccountFromPrincipal(token));

        return "redirect:/module/modulbeauftragter";
    }
}
