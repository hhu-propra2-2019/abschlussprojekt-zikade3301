package mops.module;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mops.module.database.Modul;
import mops.module.database.ModulSnapshotRepository;
import mops.module.services.SuchService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class SuchServiceTest {

    @Autowired
    SuchService suchService;

    @Autowired
    ModulSnapshotRepository modulRepo;

    Connection conn;

    @BeforeEach
    public void init() {
        try {
            conn = DriverManager.getConnection("jdbc:h2:mem:db", "root", "zikade3301");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void close() throws SQLException {
        this.conn.close();
    }

    @Test
    void searchReturnsModulObject() throws SQLException {
        List<Modul> results = new ArrayList<>();
        String title = "Programmierung";
        Modul modul = new Modul();
        modul.setTitelDeutsch(title);
        modul.setTitelEnglisch("english blabla");

        try {
            modulRepo.save(modul);
            results = suchService.searchForModuleByTitle(title, conn);

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        assertThat(results.get(0).equals(modul));
    }

    @Test
    void searchReturnsCorrectResultSize() throws SQLException {
        String title = "Programmierung";
        Modul modul = new Modul();
        modul.setTitelDeutsch(title);
        modul.setTitelEnglisch("english blabla");
        List<Modul> results = new ArrayList<>();

        try {
            modulRepo.save(modul);
            results = suchService.searchForModuleByTitle(title, conn);

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        assertThat(results.size() == 1);
    }

}

