package mops.module.database;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;
import org.hibernate.search.annotations.Field;

@Embeddable
@Data
public class Veranstaltungsbeschreibung {

    @Field
    @Column(length = 10000)
    private String inhalte;

    @Field
    @Column(length = 10000)
    private String lernergebnisse;

    @Field
    @Column(length = 10000)
    private String literatur;

    @Field
    @Column(length = 10000)
    private String verwendbarkeit;

    @Field
    @Column(length = 10000)
    private String voraussetzungenBestehen;

    @Column(length = 10000)
    private String haeufigkeit;

    @Field
    private String sprache;

}
