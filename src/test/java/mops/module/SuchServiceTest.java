package mops.module;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import mops.module.database.Modul;
import mops.module.database.ModulSnapshotRepository;
import mops.module.services.SuchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class SuchServiceTest {

    @Autowired
    SuchService suchService;
    ModulSnapshotRepository modulRepo;

    @Test
    void searchExactMatchTitle() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:mem:db", "root", "zikade3301");
            String title = "Programmierung";
            Modul modul = new Modul();
            modul.setTitelDeutsch(title);
            modul.setTitelEnglisch("english blabla");
            modulRepo.save(modul);
            List<Modul> results = suchService.searchForModuleByTitle(title, conn);

            assertThat(results.size() == 1);
            assertThat(results.get(0).equals(modul));
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
}

