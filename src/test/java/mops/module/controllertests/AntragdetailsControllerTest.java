package mops.module.controllertests;

import static mops.module.controllertests.AuthenticationTokenGenerator.generateAuthenticationToken;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.generator.ModulFaker;
import mops.module.services.AntragService;
import mops.module.services.JsonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class AntragdetailsControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @MockBean
    private AntragService antragService;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .apply(springSecurity())
                .build();
    }

    @Test
    void testAntragdetailsViewName() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("sekretariat"));

        Modul neuesModul = ModulFaker.generateFakeModul();

        Antrag neuerAntrag = new Antrag();
        neuerAntrag.setJsonModulAenderung(JsonService.modulToJsonObject(neuesModul));

        when(antragService.getAntragById((long) 3301)).thenReturn(neuerAntrag);


        mvc.perform(get("/module/antragdetails/3301"))
                .andExpect(view().name("antragdetails"));
    }

    @Test
    void testAntragdetailsAccessForAdministrator() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("sekretariat"));

        Modul neuesModul = ModulFaker.generateFakeModul();

        Antrag neuerAntrag = new Antrag();
        neuerAntrag.setJsonModulAenderung(JsonService.modulToJsonObject(neuesModul));

        when(antragService.getAntragById((long) 3301)).thenReturn(neuerAntrag);

        mvc.perform(get("/module/antragdetails/3301"))
                .andExpect(status().isOk());
    }

    @Test
    void testAntragdetailsNoAccessForOrganisator() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("orga"));

        Modul neuesModul = ModulFaker.generateFakeModul();

        Antrag neuerAntrag = new Antrag();
        neuerAntrag.setJsonModulAenderung(JsonService.modulToJsonObject(neuesModul));

        when(antragService.getAntragById((long) 3301)).thenReturn(neuerAntrag);

        assertThrows(java.lang.AssertionError.class,
                () -> {
                    mvc.perform(get("/module/antragdetails/3301"))
                            .andExpect(status().isOk());
                });
    }

    @Test
    void testAntragdetailsNoAccessForStudent() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("student"));

        Modul neuesModul = ModulFaker.generateFakeModul();

        Antrag neuerAntrag = new Antrag();
        neuerAntrag.setJsonModulAenderung(JsonService.modulToJsonObject(neuesModul));

        when(antragService.getAntragById((long) 3301)).thenReturn(neuerAntrag);

        assertThrows(java.lang.AssertionError.class,
                () -> {
                    mvc.perform(get("/module/antragdetails/3301"))
                            .andExpect(status().isOk());
                });
    }

    @Test
    void testAntragdetailsNoAccessIfNotLoggedIn() throws Exception {

        Modul modul = ModulFaker.generateFakeModul();

        Antrag neuerAntrag = new Antrag();
        neuerAntrag.setJsonModulAenderung(JsonService.modulToJsonObject(modul));

        when(antragService.getAntragById((long) 3301)).thenReturn(neuerAntrag);

        assertThrows(java.lang.AssertionError.class,
                () -> {
                    mvc.perform(get("/module/antragdetails/3301"))
                            .andExpect(status().isOk());
                });
    }

}