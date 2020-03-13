package mops.module.database;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "modul")
    private Set<Veranstaltung> veranstaltungen;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "module")
    private Set<Modulbeauftragter> modulbeauftragte;

    private String gesamtCreditPoints;

    private String studiengang;

    private Modulkategorie modulkategorie;

    private Boolean sichtbar;

    @DateTimeFormat(pattern = "dd.MM.yyyy, HH:mm:ss")
    @CreatedDate
    private LocalDateTime datumErstellung;

    @DateTimeFormat(pattern = "dd.MM.yyyy, HH:mm:ss")
    @LastModifiedDate
    private LocalDateTime datumAenderung;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "modul")
    private Set<Zusatzfeld> zusatzfelder;

    public void setVeranstaltungen(Set<Veranstaltung> veranstaltungen) {
        if (veranstaltungen == null) {
            return;
        }
        for (Veranstaltung veranstaltung : veranstaltungen) {
            veranstaltung.setModul(this);
        }
        this.veranstaltungen = veranstaltungen;
    }

    public void setModulbeauftragte(Set<Modulbeauftragter> modulbeauftragte) {
        if (modulbeauftragte == null) {
            return;
        }
        for (Modulbeauftragter modulbeauftragter : modulbeauftragte) {
            modulbeauftragter.addModul(this);
        }
        this.modulbeauftragte = modulbeauftragte;
    }

    public void setZusatzfelder(Set<Zusatzfeld> zusatzfelder) {
        if(zusatzfelder == null)
            return;
        for (Zusatzfeld zusatzfeld : zusatzfelder) {
            zusatzfeld.setModul(this);
        }
        this.zusatzfelder = zusatzfelder;
    }
}
