package mops.module.database;

import com.google.gson.annotations.Expose;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mops.module.services.Exclude;

@Entity
@Getter
@Setter
public class Zusatzfeld {

    @Id
    @GeneratedValue
    private Long id;

    private String titel;

    private String inhalt;

    @Exclude
    @ManyToOne
    private Modul modul;

}
