package mops.module.controller;

import mops.module.services.ModulService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;


@Controller
@RequestMapping("/module")
public class ModulVisibilityController {


    @Autowired
    private ModulService modulService;


    @PostMapping("/setModulVisibility")
    @Secured("ROLE_sekretariat")
    public String changeVisibilityOfModules(
            @RequestParam(name = "modulToChange") String modulToChange,
            Model model,
            KeycloakAuthenticationToken token) {

        System.out.println(modulToChange);

        model.addAttribute("account", createAccountFromPrincipal(token));


        return "redirect:/module/modulbeauftragter";
    }
}
