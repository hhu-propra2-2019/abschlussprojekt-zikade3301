package mops.module.database;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Semester {

    @Id
    @GeneratedValue
    private Long id;

    private SemesterType semestertype;

    private int year;

    @ManyToMany
    private List<Modul> module;

    @Override
    public String toString() {
        if (semestertype == SemesterType.SOMMER) {
            return "SoSe" + year;
        } else {
            return "WiSe" + year + "/" + (year + 1);
        }
    }

}
