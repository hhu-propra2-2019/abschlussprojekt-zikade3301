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
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Getter
@Setter
//TODO: Conditional Indexing: only index, when module is visible
@Indexed
//@Analyzer(impl = org.apache.lucene.analysis.de.GermanAnalyzer.class)
public class Modul {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Field
    private String titelDeutsch;

    @Field
    private String titelEnglisch;

    //Beim Löschen von Modul werden alle Veranstaltungen mitgelöscht, daher ist CascadeType.ALL
    //und FetchType.EAGER gewünscht
    @IndexedEmbedded
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "modul",
            orphanRemoval = true)
    private Set<Veranstaltung> veranstaltungen;

    //TODO: Modulbeauftragte auch durchsuchen
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> modulbeauftragte;

    private String gesamtLeistungspunkte;

    @Field
    private String studiengang;

    //TODO: das Feld auch durchsuchen?
    private Modulkategorie modulkategorie;

    private Boolean sichtbar;

    @DateTimeFormat(pattern = "dd.MM.yyyy, HH:mm:ss")
    private LocalDateTime datumErstellung;

    @DateTimeFormat(pattern = "dd.MM.yyyy, HH:mm:ss")
    private LocalDateTime datumAenderung;

    /**
     * Ruft setVeranstaltungen & setZusatzfelder auf, um das Mapping zu erneuern.
     */
    public void refreshMapping() {
        this.setVeranstaltungen(this.getVeranstaltungen());
        for (Veranstaltung v : veranstaltungen) {
            v.refreshMapping();
        }
    }

    /**
     * Fügt eine Veranstaltung zum Modul hinzu.
     *
     * @param veranstaltung Neue Veranstaltung
     */
    public void addVeranstaltung(Veranstaltung veranstaltung) {
        if (veranstaltungen == null) {
            veranstaltungen = new HashSet<>();
        }
        veranstaltungen.add(veranstaltung);
        veranstaltung.setModul(this);
    }

    /**
     * Überschreibt die Setter & erneuert die Links für die Veranstaltungen.
     *
     * @param veranstaltungen Schon vorhandenes von Veranstaltungen
     */
    public void setVeranstaltungen(Set<Veranstaltung> veranstaltungen) {
        if (veranstaltungen == null) {
            return;
        }
        for (Veranstaltung veranstaltung : veranstaltungen) {
            veranstaltung.setModul(this);
        }
        this.veranstaltungen = veranstaltungen;
    }

}
