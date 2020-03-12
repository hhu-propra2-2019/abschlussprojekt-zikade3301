package mops.module.database;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SemesterTest {

    @Test
    void testToStringSummer() {
        Semester semester = new Semester(Semestertyp.SOMMER, 2018);
        assertThat(semester.toString()).isEqualTo("SoSe2018");
    }

    @Test
    void testToStringWinter() {
        Semester semester = new Semester(Semestertyp.WINTER, 2018);
        assertThat(semester.toString()).isEqualTo("WiSe2018/19");
    }

    @Test
    void testCompareYearSmaller() {
        Semester earlierYear = new Semester(Semestertyp.SOMMER, 2018);
        Semester laterYear = new Semester(Semestertyp.SOMMER, 2019);
        assertThat(earlierYear.compareTo(laterYear)).isEqualTo(-1);
    }

    @Test
    void testCompareYearGreater() {
        Semester earlierYear = new Semester(Semestertyp.SOMMER, 2018);
        Semester laterYear = new Semester(Semestertyp.SOMMER, 2019);
        assertThat(laterYear.compareTo(earlierYear)).isEqualTo(1);
    }


    @Test
    void testCompareSemesterSmaller() {
        Semester earlierSemester = new Semester(Semestertyp.SOMMER, 2018);
        Semester laterSemester = new Semester(Semestertyp.WINTER, 2018);
        assertThat(earlierSemester.compareTo(laterSemester)).isEqualTo(-1);
    }

    @Test
    void testCompareSemesterGreater() {
        Semester earlierSemester = new Semester(Semestertyp.SOMMER, 2018);
        Semester laterSemester = new Semester(Semestertyp.WINTER, 2018);
        assertThat(laterSemester.compareTo(earlierSemester)).isEqualTo(1);
    }

    @Test
    void testCompareEqual() {
        Semester semester1 = new Semester(Semestertyp.SOMMER, 2018);
        Semester semester2 = new Semester(Semestertyp.SOMMER, 2018);
        assertThat(semester1.compareTo(semester2)).isEqualTo(0);
    }

}