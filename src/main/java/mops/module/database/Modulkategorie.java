package mops.module.database;

public enum Modulkategorie {
    PFLICHT_INFO, PFLICHT_MATHE, NEBENFACH, PRAXIS,
    WAHLPFLICHT_BA, WAHLPFLICHT_MA, BACHELORARBEIT,
    PROJEKTARBEIT, MASTERARBEIT, SONSTIGE;


    /**
     * Translates the DB-category.
     *
     * @return The German version of the category.
     */
    public String translate() {

        switch (this) {
            case PFLICHT_INFO: return "Informatik Pflichtmodul";
            case PFLICHT_MATHE: return "Mathematik Pflichtmodul";
            case NEBENFACH: return "Nebenfach";
            case PRAXIS: return "Praxis- und Berufsorientierung";
            case WAHLPFLICHT_BA: return "Wahlpflichtbereich (B.Sc.)";
            case WAHLPFLICHT_MA: return "Wahlpflichtbereich (M.Sc.)";
            case BACHELORARBEIT: return "Bachelorarbeit";
            case PROJEKTARBEIT: return "Projektarbeit";
            case MASTERARBEIT: return "Masterarbeit";
            default: return "Sonstige";
        }
    }
}
