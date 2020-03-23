package mops.module;


import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import lombok.RequiredArgsConstructor;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.services.AntragService;
import mops.module.services.JsonService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
     * @return
     */

    @RequestMapping(value = "/antragdetails/{id}", method = RequestMethod.GET)
    @Secured("ROLE_sekretariat")
    public String antragdetails(
            @PathVariable String id,
            KeycloakAuthenticationToken token,
            Model model) {

        Modul antrag = JsonService.jsonObjectToModul(antragService.getAntragById(Long.parseLong(id)).getJsonModulAenderung());

        System.out.println("--------------------------------------------"+antrag.getVeranstaltungen().size());



        model.addAttribute("veranstaltungen", antrag.getVeranstaltungen());
        model.addAttribute("account",createAccountFromPrincipal(token));
        model.addAttribute("antrag", antrag);
        return "antragdetails";
    }

    //TODO - klappt nicht

    /** Antrag annehmen.
     *
     * @param id Id des angenommenen Antrages.
     * @param antragAngenommen Antrag der von Der Rolle Sekretariat angenommen wurde.
     * @param token Der Token von keycloak für die Berechtigung.
     * @param model Modell für die HTML Datei.
     * @return Redirect zur Antragsübersicht.
     */
/*
    @PostMapping("/antragdetails/{id}")
    @Secured("ROLE_sekretariat")

 */

    @PostMapping("/antragdetails")
    @Secured("ROLE_sekretariat")
    public String antragAnnehmen(
           // @PathVariable String id,
            Modul antragAngenommen,
            Model model,
            KeycloakAuthenticationToken token) {

        String jsonModulAenderung = JsonService.modulToJsonObject(antragAngenommen);

         Antrag antrag = antragService.getAntragById(Long.parseLong("1"));
          antrag.setJsonModulAenderung(jsonModulAenderung);
          antragService.approveModulCreationAntrag(antrag);


        model.addAttribute("account",createAccountFromPrincipal(token));
        return "redirect:/module/administrator";
    }

}
