package mops.module.database;

public enum Modulkategorie {
    PFLICHT_INFO, PFLICHT_MATHE, NEBENFACH, PRAXIS,
    WAHLPFLICHT_BA, BACHELORARBEIT, WAHLPFLICHT_MA,
    PROJEKTARBEIT, MASTERARBEIT, SONSTIGE;


    /**
     * Ändert die Modulkategorie zu einer menschlich lesbareren Bezeichnung.
     *
     * @return Die deutsche lesbarere Version der Modulkategorie
     */
    public String toReadable() {

        switch (this) {
            case PFLICHT_INFO: return "Informatik Pflichtmodul";
            case PFLICHT_MATHE: return "Mathematik Pflichtmodul";
            case NEBENFACH: return "Nebenfach";
            case PRAXIS: return "Praxis- und Berufsorientierung";
            case WAHLPFLICHT_BA: return "Wahlpflichtbereich (B.Sc.)";
            case BACHELORARBEIT: return "Bachelorarbeit";
            case WAHLPFLICHT_MA: return "Wahlpflichtbereich (M.Sc.)";
            case PROJEKTARBEIT: return "Projektarbeit";
            case MASTERARBEIT: return "Masterarbeit";
            default: return "Sonstige";
        }
    }

    /**
     * Ändert für das Modulhandbuch die Modulkategorie
     * zu einer menschlich lesbareren deutschen Bezeichnung.
     *
     * @return Die deutsche lesbarere Version der Modulkategorie
     */
    public String toPdfReadableGerman() {

        switch (this) {
            case PFLICHT_INFO: return "Pflichtmodule der Informatik (1.-4. Fachsemester)";
            case PFLICHT_MATHE: return "Pflichtmodule der Mathematik (1.–4. Fachsemester)";
            case NEBENFACH: return "Nebenfachmodule";
            case PRAXIS: return "Praxis- und Berufsorientierung";
            case WAHLPFLICHT_BA: return "Lehreinheiten für Wahlpflichtbereiche (B.Sc.)";
            case BACHELORARBEIT: return "Bachelor-Arbeit";
            case WAHLPFLICHT_MA: return "Lehreinheiten für Wahlpflichtbereiche (M.Sc.)";
            case PROJEKTARBEIT: return "Projektarbeit";
            case MASTERARBEIT: return "Master-Arbeit";
            default: return "Sonstige Module";
        }
    }

    /**
     * Ändert für das Modulhandbuch die Modulkategorie
     * zu einer menschlich lesbareren englischen Bezeichnung.
     *
     * @return Die deutsche lesbarere Version der Modulkategorie
     */
    public String toPdfReadableEnglish() {
        switch (this) {
            case PFLICHT_INFO: return "Compulsory Modules in Computer Science";
            case PFLICHT_MATHE: return "Compulsory Modules in Mathematics";
            case NEBENFACH: return "Minor module";
            case PRAXIS: return "Professional Issues";
            case WAHLPFLICHT_BA: return "Courses for elective areas (B.Sc.)";
            case BACHELORARBEIT: return "Bachelor Thesis";
            case WAHLPFLICHT_MA: return "Courses for elective areas (M.Sc.)";
            case PROJEKTARBEIT: return "Individual Research Project";
            case MASTERARBEIT: return "Master’s Thesis";
            default: return "Other modules";
        }
    }
}
