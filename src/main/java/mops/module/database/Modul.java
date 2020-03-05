package mops.module.database;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Modul {

    @Id
    @GeneratedValue
    private Long id;

    private String bezeichnung;

    @Embedded
    private Modulbeschreibung beschreibung;

    @ManyToMany()
    private List<Semester> semester;

    @OneToMany
    private List<Modul> vorausgesetzteModule;

    @ManyToMany
    private List<Dozent> dozenten;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "modul")
    private List<Kurs> kurs;

}
