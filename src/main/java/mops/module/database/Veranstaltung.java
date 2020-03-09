package mops.module.database;

import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Veranstaltung {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Modul modul;

    private String titel;

    @ElementCollection
    private List<String> lehrende;

    private String creditPoints;

    @ElementCollection
    private List<String> veranstaltungsform;

    @Embedded
    private Veranstaltungsbeschreibung beschreibung;

    @OneToMany
    private List<Veranstaltung> voraussetzungenTeilnahme;

    @ManyToMany(mappedBy = "veranstaltungen")
    private List<Semester> semester;

}
