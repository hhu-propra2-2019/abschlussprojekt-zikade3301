package mops.module.database;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import lombok.Data;

@Embeddable
@Data
public class Veranstaltungsbeschreibung {

    /**
     * Konstruktor stellt sicher, dass die Sets nicht null sind.
     */
    public Veranstaltungsbeschreibung() {
        literatur = new HashSet<>();
        verwendbarkeit = new HashSet<>();
        voraussetzungenBestehen = new HashSet<>();
    }

    @Column(length = 10000)
    private String inhalte;

    @Column(length = 10000)
    private String lernergebnisse;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> literatur;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(length = 10000)
    private Set<String> verwendbarkeit;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(length = 10000)
    private Set<String> voraussetzungenBestehen;

    private String haeufigkeit;

    private String sprache;

}
