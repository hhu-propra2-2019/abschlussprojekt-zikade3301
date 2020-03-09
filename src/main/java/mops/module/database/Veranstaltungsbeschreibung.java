package mops.module.database;

import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;

@Embeddable
public class Veranstaltungsbeschreibung {

    private String inhalte;

    private String lernergebnisse;

    @ElementCollection
    private List<String> literatur;

    @ElementCollection
    private List<String> verwendbarkeit;

    @ElementCollection
    private List<String> voraussetzungenBestehen;

    private String haufigkeit;

}
