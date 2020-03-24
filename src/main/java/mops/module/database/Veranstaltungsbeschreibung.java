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

    @Column(length = 10000)
    private String inhalte;

    @Column(length = 10000)
    private String lernergebnisse;

    @Column(length = 10000)
    private String literatur;

    @Column(length = 10000)
    private String verwendbarkeit;

    @Column(length = 10000)
    private String voraussetzungenBestehen;

    private String haeufigkeit;

    private String sprache;

}
