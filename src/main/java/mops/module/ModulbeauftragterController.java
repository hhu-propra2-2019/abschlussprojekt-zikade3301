package mops.module;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import mops.module.database.Modul;
import mops.module.database.Modulbeauftragter;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;


@Controller
@SessionScope
@RequestMapping("/module")
public class ModulbeauftragterController {

    @GetMapping("/modulbeauftragter")
    @Secured("ROLE_orga")
    public String module(KeycloakAuthenticationToken token, Model model) {
        model.addAttribute("account", createAccountFromPrincipal(token));
        return "modulbeauftragter";
    }

    // TODO Erstellung in Kooperation mit DB-Team
    @RequestMapping(path = "/", method = RequestMethod.POST)
    public String result(
            @RequestParam(name = "title", required = true)
                    String titelDeutsch,
            @RequestParam(name = "titleEnglish", required = true)
                    String titelEnglisch,
            //            @RequestParam(name = "lessons", required = true)
            //                    String veranstaltungen,
            @RequestParam(name = "organizer", required = true)
                    String modulbeauftragter,
            @RequestParam(name = "cp", required = true)
                    String creditPoints,
            //            @RequestParam(name = "subject", required = true)
            //                    String studiengang,
            @RequestParam(name = "modulcategory", required = true)
                    String modulkategorie,
            //          sichtbarkeit
            //          timeStamp
            //          timeStamp
            Model model) {
        return "modulbeauftragter";
    }




}
