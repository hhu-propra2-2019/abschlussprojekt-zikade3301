package mops.module.database;

import com.google.gson.annotations.Expose;
import java.util.List;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mops.module.services.Exclude;

@Entity
@Getter
@Setter
public class Veranstaltung {

    @Id
    @GeneratedValue
    private Long id;

    @Exclude
    @ManyToOne
    private Modul modul;

    private String titel;

    @ElementCollection
    private List<String> lehrende;

    private String creditPoints;

    @ElementCollection
    private List<String> veranstaltungsformen;

    @Embedded
    private Veranstaltungsbeschreibung beschreibung;

    @OneToMany
    private Set<Veranstaltung> voraussetzungenTeilnahme;

    @ManyToMany(mappedBy = "veranstaltungen")
    private Set<Semester> semester;

}
