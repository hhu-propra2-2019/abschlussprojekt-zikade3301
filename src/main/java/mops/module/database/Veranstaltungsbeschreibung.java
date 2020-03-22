package mops.module.database;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import lombok.Data;

@Embeddable
@Data
public class Veranstaltungsbeschreibung {

    private String inhalte;

    private String lernergebnisse;

    private String literatur;

    private String verwendbarkeit;

    private String voraussetzungenBestehen;

    private String haeufigkeit;

    private String sprache;

}
