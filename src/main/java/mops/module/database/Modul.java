package mops.module.database;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@AllArgsConstructor
@Getter
@Setter
public class Modul {

    @Id
    @GeneratedValue
    private Long id;

    private String titelDeutsch;

    private String titelEnglisch;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "modul")
    private List<Veranstaltung> veranstaltungen;

    @ManyToMany(mappedBy = "module")
    private List<Modulbeauftragter> modulbeauftragte;

    private String creditPoints;

    private String studiengang;

    private Modulkategorie modulkategorie;

    private boolean sichtbar;

    @DateTimeFormat(pattern = "dd.MM.yyyy, HH:mm:ss")
    private LocalDateTime datumErstellung;

    @DateTimeFormat(pattern = "dd.MM.yyyy, HH:mm:ss")
    private LocalDateTime datumAenderung;


    public Modul() {
    }

}
