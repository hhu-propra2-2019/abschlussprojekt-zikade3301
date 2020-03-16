package mops.module;

import mops.module.services.SuchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SuchServiceTest {

    @Autowired
    SuchService suchService;


//    @Test
//    void searchExactMatchTitle() throws SQLException {
//        try {
//            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:3301/Modulhandbuch", "root", "zikade3301");
//            String title = "Programmierung";
//
//            List<Modul> results = suchService.searchForModuleByTitle(title, conn);
//            assert
//        } catch  (Exception e) {
//
//        }
//    }
}
