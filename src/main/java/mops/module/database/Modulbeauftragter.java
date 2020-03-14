package mops.module.database;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import mops.module.services.JsonExclude;

@Entity
public class Modulbeauftragter {

    public Modulbeauftragter() {
        module = new HashSet<>();
    }

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @JsonExclude
    @ManyToMany
    private Set<Modul> module;

    public void addModul(Modul modul) {
        if (module == null) {
            module = new HashSet<Modul>();
        }
        module.add(modul);
    }
}
