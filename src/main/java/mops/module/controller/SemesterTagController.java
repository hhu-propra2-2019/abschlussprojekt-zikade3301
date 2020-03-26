package mops.module.controller;

import mops.module.services.ModulService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/module")
public class SemesterTagController {


    @Autowired
    private ModulService modulService;

    @PostMapping("/semesterTag")
    @Secured("ROLE_sekretariat")
    public String addModulCreationAntrag(@RequestParam(name = "inputTag", required = true) String SemesterTag,
                                         @RequestParam(name = "idVeranstaltung") String idVeranstaltung,
                                         @RequestParam(name = "idModul") String idModul,
                                         Model model,
                                         KeycloakAuthenticationToken token) {

        modulService.tagSemesterForVeranstaltung(SemesterTag, Long.parseLong(idVeranstaltung), Long.parseLong(idModul));

        return "redirect:/module/modulbeauftragter";
    }
}