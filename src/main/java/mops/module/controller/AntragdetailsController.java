package mops.module.controller;


import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import lombok.RequiredArgsConstructor;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.services.AntragService;
import mops.module.services.JsonService;
import mops.module.services.ModulWrapperService;
import mops.module.wrapper.ModulWrapper;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.annotation.SessionScope;

@Controller
@SessionScope
@RequiredArgsConstructor
@RequestMapping("/module")
public class AntragdetailsController {

    private final AntragService antragService;

    /** Antragdetails Mapping.
     *
     * @param id Id des Antrags.
     * @param token Der Token von keycloak für die Berechtigung.
     * @param model Modell für die HTML Datei.
     * @return View antragdetails.
     */

    @RequestMapping(value = "/antragdetails/{id}", method = RequestMethod.GET)
    @Secured("ROLE_sekretariat")
    public String antragdetails(
            @PathVariable String id,
            KeycloakAuthenticationToken token,
            Model model) {

        Modul modul = JsonService.jsonObjectToModul(
                antragService.getAntragById(Long.parseLong(id)).getJsonModulAenderung());
        ModulWrapper antrag = ModulWrapperService.initializePrefilledWrapper(modul);

        model.addAttribute("antragId", id);
        model.addAttribute("account", createAccountFromPrincipal(token));
        model.addAttribute("antrag", antrag);

        return "antragdetails";
    }

    /** Antrag annehmen.
     *
     * @param id Id des angenommenen Antrages.
     * @param antragAngenommen Antrag der von Der Rolle Sekretariat angenommen wurde.
     * @param token Der Token von keycloak für die Berechtigung.
     * @param model Modell für die HTML Datei.
     * @return Redirect zur Antragsübersicht.
     */

    @PostMapping("/antragdetails/{id}")
    @Secured("ROLE_sekretariat")
    public String antragAnnehmen(
            @PathVariable String id,
            ModulWrapper antragAngenommen,
            Model model,
            KeycloakAuthenticationToken token) {

        Modul modul = ModulWrapperService.readModulFromWrapper(antragAngenommen);

        Antrag antrag = antragService.getAntragById(Long.parseLong(id));
        antrag.setJsonModulAenderung(JsonService.modulToJsonObject(modul));
        antragService.approveModulCreationAntrag(antrag);

        model.addAttribute("account",createAccountFromPrincipal(token));
        return "redirect:/module/administrator";
    }

}
