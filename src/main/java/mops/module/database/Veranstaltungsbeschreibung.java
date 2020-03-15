package mops.module.database;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import lombok.Data;

@Embeddable
/*@Getter
@Setter*/
@Data
public class Veranstaltungsbeschreibung {

    public Veranstaltungsbeschreibung() {
        literatur = new HashSet<>();
        verwendbarkeit = new HashSet<>();
        voraussetzungenBestehen = new HashSet<>();
    }

    private String inhalte;

    private String lernergebnisse;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> literatur;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> verwendbarkeit;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> voraussetzungenBestehen;

    private String haufigkeit;

    private String sprache;

}
