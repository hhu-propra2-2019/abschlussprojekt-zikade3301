package mops.module;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;

import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.services.AntragService;
import mops.module.services.JsonService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.annotation.SessionScope;

@Controller
@SessionScope
@RequiredArgsConstructor
@RequestMapping("/module")
public class AdministratorController {

    private final AntragService antragService;

    @GetMapping("/administrator")
    @Secured("ROLE_sekretariat")
    public String administrator(KeycloakAuthenticationToken token, Model model) {


        //Nur Input um zu gucken ob alles auch klappt

        String complete1Modul = "{'titelDeutsch':'Betriebssysteme','titelEnglisch':'Operating systems',"
                + "'veranstaltungen':[{'titel':'Vorlesung Betriebssysteme','leistungspunkte':'10CP'"
                + ",'veranstaltungsformen':[{'form':'Vorlesung','semesterWochenStunden':4},"
                + "{'form':'Übung','semesterWochenStunden':2}],"
                + "'beschreibung':{'inhalte':'Inhalte','lernergebnisse':'Synchronisierung',"
                + "'literatur':['Alter Schinken'],'verwendbarkeit':['Überall verwendbar'],"
                + "'voraussetzungenBestehen':['50% der Punkte in der Klausur'],"
                + "'haeufigkeit':'Alle 2 Semester','sprache':'Deutsch'},"
                + "'voraussetzungenTeilnahme':['Informatik I'],"
                + "'zusatzfelder':[{'titel':'Zusatzfeld2',"
                + "'inhalt':'Dies hier ist das zweite Zusatzfeld!'},"
                + "{'titel':'Zusatzfeld1','inhalt':'Dies hier ist das erste Zusatzfeld!'}]}],"
                + "'modulbeauftragte':['Michael Schöttner'],'gesamtLeistungspunkte':'10CP',"
                + "'studiengang':'Informatik','modulkategorie':'WAHLPFLICHT_BA'}";

        String complete2Modul = "{'titelDeutsch':'Betriebssysteme','titelEnglisch':'Operating systems',"
                + "'veranstaltungen':[{'titel':'Vorlesung Betriebssysteme','leistungspunkte':'10CP'"
                + ",'veranstaltungsformen':[{'form':'Vorlesung','semesterWochenStunden':4},"
                + "{'form':'Übung','semesterWochenStunden':2}],"
                + "'beschreibung':{'inhalte':'Inhalte','lernergebnisse':'Synchronisierung',"
                + "'literatur':['Alter Schinken'],'verwendbarkeit':['Überall verwendbar'],"
                + "'voraussetzungenBestehen':['50% der Punkte in der Klausur'],"
                + "'haeufigkeit':'Alle 2 Semester','sprache':'Deutsch'},"
                + "'voraussetzungenTeilnahme':['Informatik I'],"
                + "'zusatzfelder':[{'titel':'Zusatzfeld2',"
                + "'inhalt':'Dies hier ist das zweite Zusatzfeld!'},"
                + "{'titel':'Zusatzfeld1','inhalt':'Dies hier ist das erste Zusatzfeld!'}]}],"
                + "'modulbeauftragte':['Michael Schöttner'],'gesamtLeistungspunkte':'10CP',"
                + "'studiengang':'Informatik','modulkategorie':'WAHLPFLICHT_BA'}";

        antragService.addModulCreationAntrag(JsonService.jsonObjectToModul(complete1Modul));
        antragService.addModulCreationAntrag(JsonService.jsonObjectToModul(complete2Modul));

        //Bis hier nur Input zeug


        model.addAttribute("formatter",DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute("account", createAccountFromPrincipal(token));
        model.addAttribute("allAntraege", antragService.getAlleAntraegeGeordnetDatum());

        return "administrator";
    }


    @RequestMapping(value = "/antragdetails/{id}", method = RequestMethod.GET)
    public String moduldetails(
            @PathVariable String id,
            KeycloakAuthenticationToken token,
            Model model) {

        Modul antrag = JsonService.jsonObjectToModul(antragService.getAntragById(Long.parseLong(id)).getJsonModulAenderung());

        model.addAttribute("account",createAccountFromPrincipal(token));
        model.addAttribute("antrag", antrag);
        return "antragdetails";
    }
}

