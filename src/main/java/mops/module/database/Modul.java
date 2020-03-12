package mops.module.database;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
public class Modul {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titelDeutsch;

    private String titelEnglisch;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "modul")
    private List<Veranstaltung> veranstaltungen;

    @ManyToMany(mappedBy = "module")
    private List<Modulbeauftragter> modulbeauftragte;

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

    @OneToMany(mappedBy = "modul")
    private List<Zusatzfeld> zusatzfelder;
}
