package mops.module.database;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import lombok.Data;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;

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

    @Field
    @Column(length = 10000)
    private String inhalte;

    @Field
    @Column(length = 10000)
    private String lernergebnisse;

    @ElementCollection(fetch = FetchType.EAGER)
    @Field
    @IndexedEmbedded
    @Column(length = 10000)
    private Set<String> literatur;

    @Field
    @IndexedEmbedded
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(length = 10000)
    private Set<String> verwendbarkeit;

    @Field
    @IndexedEmbedded
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(length = 10000)
    private Set<String> voraussetzungenBestehen;

    private String haeufigkeit;

    @Field
    private String sprache;

}
