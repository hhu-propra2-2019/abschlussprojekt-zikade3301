package mops.module.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.generator.ModulFaker;
import mops.module.repositories.AntragRepository;
import mops.module.repositories.ModulSnapshotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @Autowired
    private ModulSnapshotRepository modulSnapshotRepository;

    private Modul testmodul;
    private Antrag antragToDelete;
    private List<Antrag> antraege;

    @BeforeEach
    void setUp() {

        antragService = new AntragService(antragRepository, modulSnapshotRepository);
        modulSnapshotRepository.deleteAll();
        antragRepository.deleteAll();

        testmodul = ModulFaker.generateFakeModul();
        antragToDelete = antragService.addModulCreationAntrag(testmodul, "Testmethod");
    }

    @Test
    public void stillInDbIfNotDeleted() {

        System.out.println("Test");

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
