package mops.module.database;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mops.module.services.JsonExclude;

@Entity
/*@Getter
@Setter
@EqualsAndHashCode*/
@Data
public class Veranstaltung {

    public Veranstaltung() {
        lehrende = new HashSet<>();
        veranstaltungsformen = new HashSet<>();
        voraussetzungenTeilnahme = new HashSet<>();
        semester = new HashSet<>();
    }

    @JsonExclude
    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long id;

    @JsonExclude
    @EqualsAndHashCode.Exclude
    // War zeitweise notwendig:
    // @ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne
    @JoinColumn(name = "modul_id")
    private Modul modul;

    private String titel;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> lehrende;

    private String creditPoints;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> veranstaltungsformen;

    @Embedded
    private Veranstaltungsbeschreibung beschreibung;

    // ToDo: Cascade checken
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    private Set<Veranstaltung> voraussetzungenTeilnahme;

    // ToDo: Cascade checken
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.EAGER, mappedBy = "veranstaltungen")
    private Set<Semester> semester;
}
