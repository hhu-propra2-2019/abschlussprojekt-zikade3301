package mops.module.controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;
import mops.module.database.Veranstaltungsform;
import mops.module.database.Zusatzfeld;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;


@Controller
@SessionScope
@RequestMapping("/module")
public class ModulerstellungController {


    //    TODO TEST HIERFÜR
    @GetMapping("/modulerstellung")
    @Secured("ROLE_orga")
    public String result(
            @RequestParam(name = "veranstaltungsanzahl", required = true) int veranstaltungsanzahl,
            Model model,
            KeycloakAuthenticationToken token) {
        model.addAttribute("account", createAccountFromPrincipal(token));
        model.addAttribute("veranstaltungsanzahl", veranstaltungsanzahl);
        return "modulerstellung";
    }


    //    TODO TEST HIERFÜR
    @PostMapping("/modulerstellung")
    @Secured("ROLE_orga")
    public String addCreationAntrag(@RequestParam Map<String,String> allParams) {

        int veranstaltungsanzahl = Integer.parseInt(allParams.get("veranstaltungsanzahl"));

        String titelDeutsch = allParams.get("titelDeutsch");
        String titelEnglisch = allParams.get("titelEnglisch");
        String studiengang = allParams.get("studiengang");
        int gesamtLeistungspunkte = Integer.parseInt(allParams.get("gesamtLeistungspunkte"));
        Modulkategorie modulkategorie = Modulkategorie.valueOf(allParams.get("modulkategorie"));

        Set<Veranstaltung> veranstaltungen = new HashSet<>();
        Set<String> modulbeauftragte = new HashSet<>();

        Map<String, String>[] veranstaltungsFormulare = new Map[veranstaltungsanzahl];


        for (int i = 0; i < veranstaltungsanzahl; i++) {

            int finalI = i+1;
            veranstaltungsFormulare[i] = allParams.entrySet().stream()
                    .filter(e -> e.getKey().startsWith("veranstaltung" + finalI + "_"))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//
//            String titel;
//            String leistungspunkte;
//            Set<Veranstaltungsform> veranstaltungsformen;
//            Veranstaltungsbeschreibung beschreibung;
//            String voraussetzungenTeilnahme;
//            Set<Zusatzfeld> zusatzfelder;
//            String inhalte;
//            String lernergebnisse;
//            String literatur;
//            String verwendbarkeit;
//            String voraussetzungenBestehen;
//            String haeufigkeit;
//            String sprache;
//
//            titel = veranstaltungsFormulare[i].get("veranstaltung" + i+1 + "_" + "titel");
//            leistungspunkte = veranstaltungsFormulare[i].get("veranstaltung" + i+1 + "_" + "leistungspunkte");
//            inhalte = veranstaltungsFormulare[i].get("veranstaltung" + i+1 + "_" + "inhalte");
//            lernergebnisse = veranstaltungsFormulare[i].get("veranstaltung" + i+1 + "_" + "lernergebnisse");
//            haeufigkeit = veranstaltungsFormulare[i].get("veranstaltung" + i+1 + "_" + "haeufigkeit");
//            sprache = veranstaltungsFormulare[i].get("veranstaltung" + i+1 + "_" + "sprache");
        }
        return "modulbeauftragter";
    }


}
