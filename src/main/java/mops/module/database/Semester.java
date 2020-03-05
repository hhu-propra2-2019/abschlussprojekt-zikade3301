package mops.module.database;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Entity
public class Semester implements Comparable<Semester> {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private SemesterType semestertype;

    @NonNull
    private int year;

    @ManyToMany
    private List<Modul> module;

    @Override
    public String toString() {
        if (semestertype == SemesterType.SOMMER) {
            return "SoSe" + year;
        } else {
            String nextYear = String.valueOf(year + 1);
            return "WiSe" + year + "/" + nextYear.substring(2);
        }
    }

    @Override
    public int compareTo(Semester semester) {
        if (semester.year < this.year) {
            return 1;
        }
        if (semester.year > this.year) {
            return -1;
        }
        return compareSemesterType(semester);
    }

    private int compareSemesterType(Semester semester) {
        if (semester.semestertype == this.semestertype) {
            return 0;
        }
        if (semester.semestertype == SemesterType.SOMMER && this.semestertype == SemesterType.WINTER) {
            return 1;
        }
        return -1;
    }

}
