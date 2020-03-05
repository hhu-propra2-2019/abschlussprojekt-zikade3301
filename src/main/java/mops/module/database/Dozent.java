package mops.module.database;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Dozent {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany
    private List<Modul> module;

}
