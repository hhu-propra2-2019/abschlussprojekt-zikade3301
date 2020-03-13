package mops.module.database;

import com.google.gson.annotations.Expose;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import mops.module.services.Exclude;

@Entity
public class Modulbeauftragter {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Exclude
    @ManyToMany
    private Set<Modul> module;

    public void addModul(Modul modul) {
        if (module == null) {
            module = new HashSet<Modul>();
        }
        module.add(modul);
    }
}
