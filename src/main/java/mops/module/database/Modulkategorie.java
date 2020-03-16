package mops.module.database;

public enum Modulkategorie {
    PFLICHT_INFO(1), PFLICHT_MATHE(2), NEBENFACH(3), PRAXIS(4),
    WAHLPFLICHT_BA(5), WAHLPFLICHT_MA(6), BACHELORARBEIT(7),
    PROJEKTARBEIT(8), MASTERARBEIT(9), SONSTIGE(10);

    private final int value;

    Modulkategorie(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
