package mops.module.database;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
