package mops.module.controllertests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.services.ModulService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest
@AutoConfigureMockMvc
class IndexControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ModulService modulService;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .apply(springSecurity())
                .build();
    }


    final String expect = "index";

    @Test
    void testIndexViewName() throws Exception {
        mvc.perform(get("/module/"))
                .andExpect(view().name(expect));
    }

    @Test
    void testIndexStatus() throws Exception {
        mvc.perform(get("/module/"))
                .andExpect(status().isOk());
    }

    @Test
    void testModuldetailsViewName() throws Exception {
        final String expect = "moduldetails";
        Modul testModul = new Modul();
        testModul.setTitelDeutsch("Testmodul");
        testModul.setModulkategorie(Modulkategorie.PFLICHT_INFO);

        when(modulService.getModulById((long) 3301)).thenReturn(testModul);

        mvc.perform(get("/module/moduldetails/3301"))
                .andExpect(view().name(expect));
    }

    @Test
    void testModuldetailsStatus() throws Exception {
        Modul testModul = new Modul();
        testModul.setTitelDeutsch("Testmodul");
        testModul.setModulkategorie(Modulkategorie.PFLICHT_INFO);

        when(modulService.getModulById((long) 3301)).thenReturn(testModul);

        mvc.perform(get("/module/moduldetails/3301"))
                .andExpect(status().isOk());
    }

    @Test
    void testModuldetailsShowsCorrectData() throws Exception {
        Modul testModul = new Modul();
        testModul.setTitelDeutsch("Testmodul");
        testModul.setModulkategorie(Modulkategorie.PFLICHT_INFO);

        when(modulService.getModulById((long) 3301)).thenReturn(testModul);

        mvc.perform(get("/module/moduldetails/3301"))
                .andExpect(content().string(containsString("Testmodul")));
    }
}