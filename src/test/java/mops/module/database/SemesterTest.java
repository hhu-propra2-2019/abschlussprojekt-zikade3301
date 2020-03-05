package mops.module.database;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class SemesterTest {

    @Test
    void testToStringSummer() {
        Semester semester = new Semester(SemesterType.SOMMER, 2018);
        assertThat(semester.toString()).isEqualTo("SoSe2018");
    }

    @Test
    void testToStringWinter() {
        Semester semester = new Semester(SemesterType.WINTER, 2018);
        assertThat(semester.toString()).isEqualTo("WiSe2018/19");
    }

    @Test
    void testCompareYearSmaller() {
        Semester earlierYear = new Semester(SemesterType.SOMMER, 2018);
        Semester laterYear = new Semester(SemesterType.SOMMER, 2019);
        assertThat(earlierYear.compareTo(laterYear)).isEqualTo(-1);
    }

    @Test
    void testCompareYearGreater() {
        Semester earlierYear = new Semester(SemesterType.SOMMER, 2018);
        Semester laterYear = new Semester(SemesterType.SOMMER, 2019);
        assertThat(laterYear.compareTo(earlierYear)).isEqualTo(1);
    }


    @Test
    void testCompareSemesterSmaller() {
        Semester earlierSemester = new Semester(SemesterType.SOMMER, 2018);
        Semester laterSemester = new Semester(SemesterType.WINTER, 2018);
        assertThat(earlierSemester.compareTo(laterSemester)).isEqualTo(-1);
    }

    @Test
    void testCompareSemesterGreater() {
        Semester earlierSemester = new Semester(SemesterType.SOMMER, 2018);
        Semester laterSemester = new Semester(SemesterType.WINTER, 2018);
        assertThat(laterSemester.compareTo(earlierSemester)).isEqualTo(1);
    }

    @Test
    void testCompareEqual() {
        Semester semester = new Semester(SemesterType.SOMMER, 2018);
        assertThat(semester.compareTo(semester)).isEqualTo(0);
    }

}