package mops.module.controller;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsform;
import mops.module.database.Zusatzfeld;
import mops.module.services.AntragService;
import mops.module.services.ModulService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;


@Controller
@SessionScope
@RequestMapping("/module")
public class ModulerstellungController {


    private AntragService antragService;
    private ModulService modulService;

    @Autowired
    public ModulerstellungController(AntragService antragService, ModulService modulService) {
        this.antragService = antragService;
        this.modulService = modulService;
    }

    /**
     * Mapping für das Generieren eines Modulerstellungsformulars für die eingegebene Anzahl
     * von Veranstaltungen.
     * @param veranstaltungsanzahl Anzahl der Veranstaltungen.
     * @param model Modell für die HTML-Datei.
     * @param token Keycloak-Token.
     * @return View für die Modulerstellung.
     */
    @GetMapping("/modulerstellung")
    @RolesAllowed({"ROLE_orga", "ROLE_sekretariat"})
    public String addModulCreationAntragForm(
            @RequestParam(name = "veranstaltungsanzahl", required = true) int veranstaltungsanzahl,
            Model model,
            KeycloakAuthenticationToken token) {

        //Verpacken in ein Wrapper Object
        Modul modul = new Modul();
        ModulWrapper modulWrapper = new ModulWrapper(modul, null, null, null);
        modulWrapper.initEmpty(veranstaltungsanzahl, 6, 2);

        model.addAttribute("modulWrapper", modulWrapper);
        model.addAttribute("account", createAccountFromPrincipal(token));
        model.addAttribute("modulId", null);

        return "modulerstellung";
    }

    /**
     * Mapping für das Entgegebennehmen eines Post-Requests mit den Formulardaten für die
     * Erstellung eines Modulantrags.
     * @param modulWrapper TODO
     * @param model Model für die HTML-Datei.
     * @param token Keycloak-Token.
     * @return View für die Modulerstellung.
     */
    @PostMapping("/modulerstellung")
    @RolesAllowed({"ROLE_orga", "ROLE_sekretariat"})
    public String addModulCreationAntrag(@RequestParam(name = "modulId") String modulId,
                                    ModulWrapper modulWrapper,
                                    Model model,
                                    KeycloakAuthenticationToken token) {

        if (modulId.equals("")) {
            Modul modul = ModulService.readModulFromWrapper(modulWrapper);
            String antragsteller = ((KeycloakPrincipal)token.getPrincipal()).getName();

            Antrag antrag = antragService.addModulCreationAntrag(modul, antragsteller);
            if (token.getAccount().getRoles().contains("ROLE_sekretariat")) {
                antragService.approveModulCreationAntrag(antrag);
                return "modulbeauftragter";
            }
            model.addAttribute("account", createAccountFromPrincipal(token));

            return "modulbeauftragter";
        } else {
            Long modulIdLong = Long.parseLong(modulId);
            Modul altesModul = modulService.getModulById(modulIdLong);
            Modul neuesModul = ModulService.readModulFromWrapper(modulWrapper);

            neuesModul.setId(modulIdLong);
            neuesModul.refreshMapping();
            Modul diffModul = ModulService.calculateModulDiffs(altesModul, neuesModul);

            if (diffModul != null) {
                String antragsteller = ((KeycloakPrincipal) token.getPrincipal()).getName();
                Antrag antrag = antragService.addModulModificationAntrag(neuesModul, antragsteller);
                if (token.getAccount().getRoles().contains("ROLE_sekretariat")) {
                    antragService.approveModulModificationAntrag(antrag);
                    return "modulbeauftragter";
                }
            } //else {
            //return "error"; //TODO
            //}
            model.addAttribute("account", createAccountFromPrincipal(token));

            return "modulbeauftragter";
        }
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

        //Verpacken in ein Wrapper Object
        ModulWrapper modulWrapper = new ModulWrapper(modul, null, null, null);
        modulWrapper.initPrefilled(6, 2);
        model.addAttribute("modulWrapper", modulWrapper);
        model.addAttribute("modulId", id);

        return "modulerstellung";
    }

}
