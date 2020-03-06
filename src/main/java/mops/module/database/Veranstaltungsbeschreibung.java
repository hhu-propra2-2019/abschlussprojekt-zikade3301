package mops.module.database;

import java.util.List;
import javax.persistence.Embeddable;

@Embeddable
public class Veranstaltungsbeschreibung {

    private String inhalte;
    private String lernergebnisse;
    //    private List<String> literatur;
    //    private List<String> verwendbarkeit;
    //    private List<String> voraussetzungenBestehen;
    //TODO: bei regelmäßig angebotenen Modulen automatisiert Semester hinzufügen?
    private String haufigkeit;

}
