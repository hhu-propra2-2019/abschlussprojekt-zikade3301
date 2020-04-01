package mops.module.controller;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import javax.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.services.AntragService;
import mops.module.services.ModulService;
import mops.module.services.ModulWrapperService;
import mops.module.wrapper.ModulWrapper;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;


@Controller
@SessionScope
@RequiredArgsConstructor
@RequestMapping("/module")
public class ModulerstellungController {

    private final ModulService modulService;
    private final AntragService antragService;


    /**
     * Get-Mapping für das Generieren eines Modulerstellungsformulars für die eingegebene Anzahl
     * von Veranstaltungen.
     * @param veranstaltungsanzahl Anzahl der Veranstaltungen.
     * @param model Modell für die HTML-Datei.
     * @param token Der Token von keycloak für die Berechtigung.
     * @return View für die Modulerstellung.
     */
    @GetMapping("/modulerstellung")
    @RolesAllowed({"ROLE_orga", "ROLE_sekretariat"})
    public String addModulCreationAntragForm(
            @RequestParam(name = "veranstaltungsanzahl") int veranstaltungsanzahl,
            Model model,
            KeycloakAuthenticationToken token) {

        ModulWrapper modulWrapper =
                ModulWrapperService.initializeEmptyWrapper(veranstaltungsanzahl);

        model.addAttribute("modulWrapper", modulWrapper);
        model.addAttribute("account", createAccountFromPrincipal(token));
        model.addAttribute("modulId", null);

        return "modulerstellung";
    }

    /**
     * Post-Mapping für die Formulardaten für die Erstellung eines Modulantrags.
     * @param modulWrapper Wrapper für ein Modul und seine Unter-Objekte
     * @param model Model für die HTML-Datei.
     * @param token Keycloak-Token.
     * @return View für die Modulerstellung.
     */
    @PostMapping("/modulerstellung")
    @RolesAllowed({"ROLE_orga", "ROLE_sekretariat"})
    public String modulCreationAntragPreview(@RequestParam(name = "modulId") String modulId,
                                         ModulWrapper modulWrapper,
                                         Model model,
                                         KeycloakAuthenticationToken token) {

        model.addAttribute("account", createAccountFromPrincipal(token));
        model.addAttribute("allCategories", Modulkategorie.values());
        model.addAttribute("allModules", modulService.getAllModule());

        Modul modul = ModulWrapperService.readModulFromWrapper(modulWrapper);
        model.addAttribute("modul", modul);
        model.addAttribute("modulWrapper", modulWrapper);
        model.addAttribute("modulId", modulId);
        return "modulpreview";

    }

    @PostMapping("/modulerstellung_confirmation")
    @RolesAllowed({"ROLE_orga", "ROLE_sekretariat"})
    public String modulCreationAntragConfirm(@RequestParam(name = "modulId") String modulId,
                                         ModulWrapper modulWrapper,
                                         Model model,
                                         KeycloakAuthenticationToken token) {

        String antragsteller = ((KeycloakPrincipal)token.getPrincipal()).getName();
        model.addAttribute("account", createAccountFromPrincipal(token));
        model.addAttribute("allCategories", Modulkategorie.values());
        model.addAttribute("allModules", modulService.getAllModule());

        Modul modul = ModulWrapperService.readModulFromWrapper(modulWrapper);

        Antrag antrag = antragService.addModulCreationAntrag(modul, antragsteller);
        if (token.getAccount().getRoles().contains("sekretariat")) {
            antragService.approveModulCreationAntrag(antrag);
        }

        return "modulbeauftragter";

    }

    @PostMapping("/modulerstellung_back_to_edit")
    @RolesAllowed({"ROLE_orga", "ROLE_sekretariat"})
    public String modulCreationAntragBackToEdit(@RequestParam(name = "modulId") String modulId,
                                                     ModulWrapper modulWrapper,
                                                     Model model,
                                                     KeycloakAuthenticationToken token) {

        Modul modul = ModulWrapperService.readModulFromWrapper(modulWrapper);

        ModulWrapper refilledModulWrapper =
                ModulWrapperService.initializePrefilledWrapper(modul);

        model.addAttribute("modulWrapper", refilledModulWrapper);
        model.addAttribute("account", createAccountFromPrincipal(token));
        model.addAttribute("modulId", null);

        return "modulerstellung";

    }

    /**
     * Post-Mapping für die Formulardaten für die Erstellung eines Modulantrags.
     * @param modulWrapper Wrapper für ein Modul und seine Unter-Objekte
     * @param model Model für die HTML-Datei.
     * @param token Keycloak-Token.
     * @return View für die Modulerstellung.
     */
    @PostMapping("/modulbearbeitung")
    @RolesAllowed({"ROLE_orga", "ROLE_sekretariat"})
    public String addModulModificationAntrag(@RequestParam(name = "modulId") String modulId,
                                         ModulWrapper modulWrapper,
                                         Model model,
                                         KeycloakAuthenticationToken token) {

        String antragsteller = ((KeycloakPrincipal)token.getPrincipal()).getName();
        model.addAttribute("account", createAccountFromPrincipal(token));
        model.addAttribute("allCategories", Modulkategorie.values());
        model.addAttribute("allModules", modulService.getAllModule());

        Long modulIdLong = Long.parseLong(modulId);
        Modul altesModul = modulService.getModulById(modulIdLong);
        Modul neuesModul = ModulWrapperService.readModulFromWrapper(modulWrapper);

        neuesModul.setId(modulIdLong);
        neuesModul.refreshMapping();
        Modul diffModul = ModulService.calculateModulDiffs(altesModul, neuesModul);

        if (diffModul != null) {
            Antrag antrag = antragService.addModulModificationAntrag(neuesModul, antragsteller);
            if (token.getAccount().getRoles().contains("sekretariat")) {
                antragService.approveModulModificationAntrag(antrag);
                return "modulbeauftragter";
            }
        }

        return "modulbeauftragter";

    }



    /**
     * Mapping für das Generieren eines Modulbearbeitungsformulars für die eingegebene Modul-Id.
     * @param id id des zu bearbeitenden Moduls.
     * @param model Modell für die HTML-Datei.
     * @param token Keycloak-Token.
     * @return View für die Modulbearbeitung.
     */
    @GetMapping("/modulbearbeitung/{id}")
    @RolesAllowed({"ROLE_orga", "ROLE_sekretariat"})
    public String addModulModificationAntragForm(
            @PathVariable String id,
            Model model,
            KeycloakAuthenticationToken token) {
        model.addAttribute("account", createAccountFromPrincipal(token));
        Modul modul = modulService.getModulById(Long.parseLong(id));
        ModulWrapper modulWrapper = ModulWrapperService.initializePrefilledWrapper(modul);

        model.addAttribute("modulWrapper", modulWrapper);
        model.addAttribute("modulId", id);

        return "modulerstellung";
    }

}
