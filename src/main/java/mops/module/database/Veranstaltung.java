package mops.module.database;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Veranstaltung {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Modul modul;

    private String titel;

    //    private List<String> lehrende;

    private double creditPoints;

    private String sprache; //TODO: als Kategorie?

    //    private List<String> veranstaltungsform;

    @Embedded
    private Veranstaltungsbeschreibung beschreibung;

    //    private List<Veranstaltung> voraussetzungenTeilnahme;

    @ManyToMany(mappedBy = "veranstaltungen")
    private List<Semester> semester;

}
