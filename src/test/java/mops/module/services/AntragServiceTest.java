package mops.module.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.generator.ModulFaker;
import mops.module.repositories.AntragRepository;
import mops.module.repositories.ModulSnapshotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class AntragServiceTest {


    private AntragService antragService;

    @Autowired
    private AntragRepository antragRepository;

//    @MockBean
    private ModulSnapshotRepository modulSnapshotRepository;

    private Modul testmodul;
    private Antrag antragToDelete;
    private List<Antrag> antraege;

    @BeforeEach
    void setUp() {
        modulSnapshotRepository = mock(ModulSnapshotRepository.class);
        antragService = new AntragService(antragRepository, modulSnapshotRepository);
        modulSnapshotRepository.deleteAll();
        antragRepository.deleteAll();

        testmodul = ModulFaker.generateFakeModul();
        antragToDelete = antragService.addModulCreationAntrag(testmodul, "Testmethod");
    }

    @Test
    public void stillInDbIfNotDeleted() {

        antraege = antragService.getAlleAntraege();

        assertThat(antraege.size()).isEqualTo(1);
    }

    @Test
    public void deleteAntragTest() {

        antragService.deleteAntrag(antragToDelete.getId());
        antraege = antragService.getAlleAntraege();

        assertThat(antraege.size()).isEqualTo(0);
    }

}
