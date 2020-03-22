package mops.module.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;
import mops.module.database.Veranstaltungsform;
import mops.module.database.Zusatzfeld;
import mops.module.services.AntragService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import static mops.module.keycloak.KeycloakMopsAccount.createAccountFromPrincipal;


@Controller
@SessionScope
@RequestMapping("/module")
public class ModulerstellungController {


    private AntragService antragService;

    @Autowired
    public ModulerstellungController(AntragService antragService) {
        this.antragService = antragService;
    }

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
    public String addCreationAntrag(@RequestBody Map<String,String> allParams,
                                    Model model,
                                    KeycloakAuthenticationToken token) {

        //Einlesen der Formulardaten für Modul allgemein
        String titelDeutsch = allParams.get("titelDeutsch");
        String titelEnglisch = allParams.get("titelEnglisch");
        String studiengang = allParams.get("studiengang");
        String gesamtLeistungspunkte = allParams.get("gesamtLeistungspunkte");
        String modulbeauftragte = allParams.get("modulbeauftragte");
        Modulkategorie modulkategorie = Modulkategorie.valueOf(allParams.get("modulkategorie"));

        //Separieren der Formulardaten für einzelne Veranstaltungen
        int veranstaltungsanzahl = Integer.parseInt(allParams.get("veranstaltungsanzahl"));
        Set<Veranstaltung> veranstaltungen = new HashSet<>();
        for (int i = 0; i < veranstaltungsanzahl; i++) {
            int finalI = i + 1;
            Map<String, String> veranstaltungsFormular = allParams.entrySet().stream()
                    .filter(e -> e.getKey().startsWith("veranstaltung" + finalI + "_"))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            String titel = veranstaltungsFormular.get("veranstaltung" + finalI + "_" + "titel");
            String leistungspunkte = veranstaltungsFormular.get("veranstaltung" + finalI
                    + "_" + "leistungspunkte");
            String zusatzfeld1Titel = veranstaltungsFormular.get("veranstaltung" + finalI
                    + "_" + "zusatzfeld1_titel");
            String zusatzfeld1Inhalt = veranstaltungsFormular.get("veranstaltung" + finalI
                    + "_" + "zusatzfeld1_inhalt");
            String zusatzfeld2Titel = veranstaltungsFormular.get("veranstaltung" + finalI
                    + "_" + "zusatzfeld2_titel");
            String zusatzfeld2Inhalt = veranstaltungsFormular.get("veranstaltung" + finalI
                    + "_" + "zusatzfeld2_inhalt");
            String inhalte = veranstaltungsFormular.get("veranstaltung" + finalI
                    + "_" + "inhalte");
            String lernergebnisse = veranstaltungsFormular.get("veranstaltung" + finalI
                    + "_" + "lernergebnisse");
            String literatur = veranstaltungsFormular.get("veranstaltung" + finalI
                    + "_" + "literatur");
            String verwendbarkeit = veranstaltungsFormular.get("veranstaltung" + finalI
                    + "_" + "verwendbarkeit");
            String voraussetzungenBestehen = veranstaltungsFormular.get("veranstaltung" + finalI
                    + "_" + "voraussetzungenBestehen");
            String haeufigkeit = veranstaltungsFormular.get("veranstaltung" + finalI
                    + "_" + "haeufigkeit");
            String sprache = veranstaltungsFormular.get("veranstaltung" + finalI
                    + "_" + "sprache");
            Veranstaltung veranstaltung = new Veranstaltung();
            veranstaltung.setTitel(titel);
            veranstaltung.setLeistungspunkte(leistungspunkte);
            Set<Zusatzfeld> zusatzfelder = new HashSet<>();
            if (!zusatzfeld1Titel.equals("") || !zusatzfeld1Inhalt.equals("")) {
                Zusatzfeld zusatzfeld = new Zusatzfeld();
                zusatzfeld.setTitel(zusatzfeld1Titel);
                zusatzfeld.setInhalt(zusatzfeld1Inhalt);
                zusatzfelder.add(zusatzfeld);
            }
            if (!zusatzfeld2Titel.equals("") || !zusatzfeld2Inhalt.equals("")) {
                Zusatzfeld zusatzfeld = new Zusatzfeld();
                zusatzfeld.setTitel(zusatzfeld2Titel);
                zusatzfeld.setInhalt(zusatzfeld2Inhalt);
                zusatzfelder.add(zusatzfeld);
            }
            veranstaltung.setZusatzfelder(zusatzfelder);
            Veranstaltungsbeschreibung beschreibung = new Veranstaltungsbeschreibung();
            beschreibung.setInhalte(inhalte);
            beschreibung.setLernergebnisse(lernergebnisse);
            beschreibung.setLiteratur(literatur);
            beschreibung.setVerwendbarkeit(verwendbarkeit);
            beschreibung.setVoraussetzungenBestehen(voraussetzungenBestehen);
            beschreibung.setHaeufigkeit(haeufigkeit);
            beschreibung.setSprache(sprache);
            veranstaltung.setBeschreibung(beschreibung);
            veranstaltungen.add(veranstaltung);
        }

        Modul modul = new Modul();
        modul.setTitelDeutsch(titelDeutsch);
        modul.setTitelEnglisch(titelEnglisch);
        modul.setStudiengang(studiengang);
        modul.setGesamtLeistungspunkte(gesamtLeistungspunkte);
        modul.setModulbeauftragte(modulbeauftragte);
        modul.setModulkategorie(modulkategorie);
        modul.setVeranstaltungen(veranstaltungen);

//            Set<Zusatzfeld> zusatzfelder;

        antragService.addModulCreationAntrag(modul,
                ((KeycloakPrincipal)token.getPrincipal()).getName());
        return "modulbeauftragter";
    }


}
