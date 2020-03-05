package mops.module.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Kurs {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private Modul modul;

}
