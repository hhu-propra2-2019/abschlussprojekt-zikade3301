package mops.module.controller;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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


//    TODO TEST HIERFÜR
    @GetMapping("/modulerstellung")
    @Secured("ROLE_orga")
    public String result(
            @RequestParam(name = "veranstaltungsanzahl", required = true) int veranstaltungsanzahl,
            Model model,
            KeycloakAuthenticationToken token) {
        model.addAttribute("account", createAccountFromPrincipal(token));
        return "modulerstellung";
    }




}
