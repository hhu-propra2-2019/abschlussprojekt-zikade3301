package mops.module.database;

import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mops.module.services.Exclude;

@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Semester implements Comparable<Semester> {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private Semestertyp semestertyp;

    @NonNull
    private int jahr;

    @Exclude
    @ManyToMany
    private Set<Veranstaltung> veranstaltungen;

    @Override
    public String toString() {
        if (semestertyp == Semestertyp.SOMMER) {
            return "SoSe" + jahr;
        } else {
            String nextYear = String.valueOf(jahr + 1);
            return "WiSe" + jahr + "/" + nextYear.substring(2);
        }
    }

    @Override
    public int compareTo(Semester semester) {
        if (semester.jahr < this.jahr) {
            return 1;
        }
        if (semester.jahr > this.jahr) {
            return -1;
        }
        return compareSemesterType(semester);
    }

    private int compareSemesterType(Semester semester) {
        if (semester.semestertyp == this.semestertyp) {
            return 0;
        }
        if (semester.semestertyp == Semestertyp.SOMMER
                && this.semestertyp == Semestertyp.WINTER) {
            return 1;
        }
        return -1;
    }

}
