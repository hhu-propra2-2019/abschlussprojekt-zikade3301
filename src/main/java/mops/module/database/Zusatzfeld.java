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
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;

@Entity
@Data
public class Zusatzfeld {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Field
    private String titel;

    @Column(length = 10000)
    @Field
    private String inhalt;

    @ContainedIn
    @JsonExclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    private Veranstaltung veranstaltung;

}
