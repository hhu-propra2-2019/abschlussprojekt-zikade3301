package mops.module.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mops.module.services.JsonExclude;

@Entity
@Data
public class Veranstaltungsform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String form;

    private int semesterWochenStunden;

    @JsonExclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    private Veranstaltung veranstaltung;

}
