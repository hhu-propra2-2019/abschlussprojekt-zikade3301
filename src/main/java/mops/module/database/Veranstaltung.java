package mops.module.database;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import mops.module.services.JsonExclude;

@Entity
@Getter
@Setter
public class Veranstaltung {

    /**
     *  Konstruktor stellt sicher, dass die Sets nicht null sind.
     */
    public Veranstaltung() {
        veranstaltungsformen = new HashSet<>();
        semester = new HashSet<>();
        zusatzfelder = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonExclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "modul_id")
    private Modul modul;

    private String titel;

    private String leistungspunkte;

    //Beim Löschen von Veranstaltung werden alle Veranstaltungsformen mitgelöscht
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "veranstaltung",
            orphanRemoval = true)
    private Set<Veranstaltungsform> veranstaltungsformen;

    @Embedded
    private Veranstaltungsbeschreibung beschreibung;

    private String voraussetzungenTeilnahme;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> semester;

    //Beim Löschen von Veranstaltung werden alle Zusatzfelder mitgelöscht
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "veranstaltung",
            orphanRemoval = true)
    private Set<Zusatzfeld> zusatzfelder;

    public void refreshMapping() {
        this.setZusatzfelder(this.getZusatzfelder());
        this.setVeranstaltungsformen(this.getVeranstaltungsformen());
    }

    /**
     * Überschreibt die Setter & erneuert die Links für die Zusatzfelder.
     *
     * @param zusatzfelder Schon vorhandenes Set von Zusatzfelder
     */
    public void setZusatzfelder(Set<Zusatzfeld> zusatzfelder) {
        if (zusatzfelder == null) {
            return;
        }
        for (Zusatzfeld zusatzfeld : zusatzfelder) {
            zusatzfeld.setVeranstaltung(this);
        }
        this.zusatzfelder = zusatzfelder;
    }

    /**
     * Überschreibt die Setter & erneuert die Links für die Veranstaltungsformen.
     *
     * @param veranstaltungsformen Schon vorhandenes Set von Veranstaltungsformen
     */
    public void setVeranstaltungsformen(Set<Veranstaltungsform> veranstaltungsformen) {
        if (veranstaltungsformen == null) {
            return;
        }
        for (Veranstaltungsform veranstaltungsform : veranstaltungsformen) {
            veranstaltungsform.setVeranstaltung(this);
        }
        this.veranstaltungsformen = veranstaltungsformen;
    }

}
