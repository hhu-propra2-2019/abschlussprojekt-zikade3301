package mops.module.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;

import java.util.List;
import java.util.Set;
import mops.module.database.Modul;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsform;
import mops.module.generator.ModulFaker;
import mops.module.wrapper.ModulWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class ModulWrapperServiceTest {

    private Modul completeModul;

    @BeforeEach
    void init() {
        completeModul = ModulFaker.generateFakeModul();
    }

    @Test
    void initializeEmptyWrapperNumberOfVeranstaltungen() {
        ModulWrapper modulWrapper = ModulWrapperService.initializeEmptyWrapper(3);
        int veranstaltungenInWrapper = modulWrapper.getVeranstaltungen().size();
        assertThat(veranstaltungenInWrapper).isEqualTo(3);
    }

    @Test
    void initializeEmptyWrapperNumberOfListsInArray() {
        ModulWrapper modulWrapper = ModulWrapperService.initializeEmptyWrapper(3);
        int numberOfListsInVeranstaltungsformenListArray =
                modulWrapper.getVeranstaltungsformen().length;
        assertThat(numberOfListsInVeranstaltungsformenListArray).isEqualTo(3);
    }

    @Test
    void initializeEmptyWrapperNumberOfVeranstaltungsformenPerList() {
        ModulWrapper modulWrapper = ModulWrapperService.initializeEmptyWrapper(2);
        int numberOfVeranstaltungsformenInFirstList =
                modulWrapper.getVeranstaltungsformen()[0].size();
        int expectedNumber = ModulWrapperService.VERANSTALTUNGSFORMEN_PRO_VERANSTALTUNG;
        assertThat(numberOfVeranstaltungsformenInFirstList).isEqualTo(expectedNumber);
    }

    @Test
    void initializeEmptyWrapperNumberOfZusatzfelderPerList() {
        ModulWrapper modulWrapper = ModulWrapperService.initializeEmptyWrapper(2);
        int numberOfZusatzfelderInSecondList =
                modulWrapper.getZusatzfelder()[1].size();
        int expectedNumber = ModulWrapperService.ZUSATZFELDER_PRO_VERANSTALTUNG;
        assertThat(numberOfZusatzfelderInSecondList).isEqualTo(expectedNumber);
    }

    @Test
    void initializePrefilledWrapperNumberOfVeranstaltungen() {
        ModulWrapper modulWrapper = ModulWrapperService.initializePrefilledWrapper(completeModul);
        int numberOfListsInVeranstaltungsformenListArray =
                modulWrapper.getVeranstaltungsformen().length;
        int expectedNumber = completeModul.getVeranstaltungen().size();
        assertThat(numberOfListsInVeranstaltungsformenListArray).isEqualTo(expectedNumber);
    }

    @Test
    void initializePrefilledWrapperNumberOfVeranstaltungsformenPerList() {
        ModulWrapper modulWrapper = ModulWrapperService.initializePrefilledWrapper(completeModul);
        int numberOfVeranstaltungsformenInFirstList =
                modulWrapper.getVeranstaltungsformen()[0].size();
        int expectedNumber = ModulWrapperService.VERANSTALTUNGSFORMEN_PRO_VERANSTALTUNG;
        assertThat(numberOfVeranstaltungsformenInFirstList).isEqualTo(expectedNumber);
    }

    @Test
    void initializePrefilledWrapperNumberOfZusatzfelderPerList() {
        ModulWrapper modulWrapper = ModulWrapperService.initializePrefilledWrapper(completeModul);
        int numberOfZusatzfelderInSecondList =
                modulWrapper.getZusatzfelder()[0].size();
        int expectedNumber = ModulWrapperService.ZUSATZFELDER_PRO_VERANSTALTUNG;
        assertThat(numberOfZusatzfelderInSecondList).isEqualTo(expectedNumber);
    }

    @Test
    void initializePrefilledWrapperNumberOfVeranstaltungsformenPerListWhenInputNullList() {
        Veranstaltung veranstaltung = completeModul.getVeranstaltungen()
                .stream().findFirst().orElse(null);
        completeModul.getVeranstaltungen().removeIf(v -> v != veranstaltung);
        veranstaltung.setVeranstaltungsformen(null);
        ModulWrapper modulWrapper = ModulWrapperService.initializePrefilledWrapper(completeModul);
        int numberOfVeranstaltungsformenInFirstList =
                modulWrapper.getVeranstaltungsformen()[0].size();
        int expectedNumber = ModulWrapperService.VERANSTALTUNGSFORMEN_PRO_VERANSTALTUNG;
        assertThat(numberOfVeranstaltungsformenInFirstList).isEqualTo(expectedNumber);
    }


    @Test
    void initializePrefilledWrapperKeepsExistingVeranstaltungsformen() {
        Veranstaltung veranstaltungInModul = completeModul.getVeranstaltungen()
                .stream().findFirst().orElse(null);
        completeModul.getVeranstaltungen().removeIf(v -> v != veranstaltungInModul);
        Set<Veranstaltungsform> veranstaltungsformenInModul =
                veranstaltungInModul.getVeranstaltungsformen();

        long i = 1;
        for (Veranstaltungsform v : veranstaltungsformenInModul) {
            v.setId(i);
            i++;
        }

        ModulWrapper modulWrapper = ModulWrapperService.initializePrefilledWrapper(completeModul);
        List<Veranstaltungsform> veranstaltungsformenInWrapper =
                modulWrapper.getVeranstaltungsformen()[0];

        for (Veranstaltungsform veranstaltungsform : veranstaltungsformenInModul) {
            org.hamcrest.MatcherAssert.assertThat(veranstaltungsformenInWrapper,
                    hasItem(veranstaltungsform));
        }

    }

    @Test
    void readModulFromWrapperFullRoutineNumberOfVeranstaltungen() {
        int numberOfVeranstaltungenInModul = completeModul.getVeranstaltungen().size();
        ModulWrapper modulWrapper = ModulWrapperService.initializePrefilledWrapper(completeModul);
        Modul unwrappedModul = ModulWrapperService.readModulFromWrapper(modulWrapper);
        int numberOfVeranstaltungenInUnwrappedModul = unwrappedModul.getVeranstaltungen().size();
        assertThat(numberOfVeranstaltungenInUnwrappedModul)
                .isEqualTo(numberOfVeranstaltungenInModul);
    }

    @Test
    void readModulFromWrapperFullRoutineContainsAllVeranstaltungen() {
        Set<Veranstaltung> veranstaltungenInModul = completeModul.getVeranstaltungen();
        ModulWrapper modulWrapper = ModulWrapperService.initializePrefilledWrapper(completeModul);
        Modul unwrappedModul = ModulWrapperService.readModulFromWrapper(modulWrapper);
        Set<Veranstaltung> veranstaltungenInUnwrappedModul = unwrappedModul.getVeranstaltungen();

        long i = 1;
        for (Veranstaltung v : veranstaltungenInModul) {
            v.setId(i);
            i++;
        }

        assertThat(veranstaltungenInUnwrappedModul).containsAll(veranstaltungenInModul);
    }

}