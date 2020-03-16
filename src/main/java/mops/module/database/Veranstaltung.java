package mops.module.database;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mops.module.services.JsonExclude;

@Entity
@Data
public class Veranstaltung {

    /**
     *
     */
    public Veranstaltung() {
        veranstaltungsformen = new HashSet<>();
        voraussetzungenTeilnahme = new HashSet<>();
        semester = new HashSet<>();
        lehrende = new HashSet<>();
    }

    //@JsonExclude
    //@EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long id;

    @JsonExclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "modul_id")
    private Modul modul;

    private String titel;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> lehrende;

    private String leistungspunkte;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> veranstaltungsformen;

    @Embedded
    private Veranstaltungsbeschreibung beschreibung;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> voraussetzungenTeilnahme;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> semester;
}
