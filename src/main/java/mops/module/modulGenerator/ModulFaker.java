package mops.module.modulGenerator;

import mops.module.database.Modul;

public class ModulFaker {
    // modul
    private static final String[] titelDeutsch = {null};
    private static final String[] titelEnglish = {null};
    // veranstaltung Entität
    private static final String[] gesamtLeistungspunkte = {null};
    private static final String[] studiengang = {null};
    private static final String[] modulkategorrie = {null};
    private static final Boolean[] modulSichtbar={true};
    // zusatzfelder Entität
    //
    //// veranstaltung
    private static final String veranstaltungTitel[] = {null};
    private static final String lehrende[] = {null};
    private static final String leistungspunkte[] = {null};
    private static final String veranstaltungsFormen[] = {null};
    //// beshcreibung embedded
    //// voraussetzungenTeilnahme -> mehrere veranstaltungTitels
    private static final String semester[] = {null};
    private static final String jahr[]={null};
    //
    ////// Veranstaltungsbeschreibung
    private static final String inhalt[]={null};
    private static final String lernergebnisse[]={null};
    private static final String literatur[]={null};
    private static final String verwendbarkeit[]={null};
    private static final String voraussetzungenBestehen[]={null};
    private static final String haeufigkeit[]={null};
    private static final String sprache[]={null};
    //// Zusatzfeld
    private static final String titel[]={null};
    private static final String ZusatzfeldInhalt[]={null};
    //
    public static Modul generateFakeModul(){
        Modul fakeModul=new Modul();
        fakeModul.setTitelDeutsch(null);
        return null;
    }

}
