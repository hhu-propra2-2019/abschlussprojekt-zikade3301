package mops.module.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Zusatzfeld {

    @Id
    @GeneratedValue
    private Long id;

    private String titel;

    private String inhalt;

    @ManyToOne
    private Modul modul;

}
