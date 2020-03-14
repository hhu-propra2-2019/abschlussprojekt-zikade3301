package mops.module.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import mops.module.services.JsonExclude;

@Entity
@Getter
@Setter
public class Zusatzfeld {

    @JsonExclude
    @Id
    @GeneratedValue
    private Long id;

    private String titel;

    private String inhalt;

    @JsonExclude
    @ManyToOne
    private Modul modul;

}
