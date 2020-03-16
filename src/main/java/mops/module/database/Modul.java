package mops.module.database;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Getter
@Setter
public class Modul {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titelDeutsch;

    private String titelEnglisch;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "modul",
            orphanRemoval = true)
    private Set<Veranstaltung> veranstaltungen;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> modulbeauftragte;

    private String gesamtLeistungspunkte;

    private String studiengang;

    private Modulkategorie modulkategorie;

    private Boolean sichtbar;

    @DateTimeFormat(pattern = "dd.MM.yyyy, HH:mm:ss")
    private LocalDateTime datumErstellung;

    @DateTimeFormat(pattern = "dd.MM.yyyy, HH:mm:ss")
    private LocalDateTime datumAenderung;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "modul",
            orphanRemoval = true)
    private Set<Zusatzfeld> zusatzfelder;

    public void refreshLinks() {
        this.setVeranstaltungen(this.getVeranstaltungen());
        this.setZusatzfelder(this.getZusatzfelder());
    }

    /**
     *
     * @param veranstaltung
     */
    public void addVeranstaltung(Veranstaltung veranstaltung) {
        if (veranstaltungen == null) {
            veranstaltungen = new HashSet<>();
        }
        veranstaltungen.add(veranstaltung);
        veranstaltung.setModul(this);
    }

    public void setVeranstaltungen(Set<Veranstaltung> veranstaltungen) {
        if (veranstaltungen == null) {
            return;
        }
        for (Veranstaltung veranstaltung : veranstaltungen) {
            veranstaltung.setModul(this);
        }
        this.veranstaltungen = veranstaltungen;
    }

    public void setZusatzfelder(Set<Zusatzfeld> zusatzfelder) {
        if (zusatzfelder == null) {
            return;
        }
        for (Zusatzfeld zusatzfeld : zusatzfelder) {
            zusatzfeld.setModul(this);
        }
        this.zusatzfelder = zusatzfelder;
    }
}
