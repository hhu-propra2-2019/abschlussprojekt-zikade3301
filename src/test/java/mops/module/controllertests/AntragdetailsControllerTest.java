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
import mops.module.services.ModulService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
class AntragdetailsControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @MockBean
    private AntragService antragService;
    @MockBean
    private ModulService modulService;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .apply(springSecurity())
                .build();

        Modul modulAusAntrag = ModulFaker.generateFakeModul();

        Modul altesModul = modulAusAntrag;

        Antrag neuerAntrag = new Antrag();
        neuerAntrag.setJsonModulAenderung(JsonService.modulToJsonObject(modulAusAntrag));

        when(antragService.getAntragById((long) 3301)).thenReturn(neuerAntrag);
        when(modulService.getModulById(neuerAntrag.getModulId())).thenReturn(altesModul);

    }

    @Test
    void testkreationsAntragsDetailsViewName() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("sekretariat"));

        mvc.perform(get("/module/kreationsAntragsDetails/3301"))
                .andExpect(view().name("antragdetails"));
    }

    @Test
    void testkreationsAntragsDetailsAccessForAdministrator() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("sekretariat"));

        mvc.perform(get("/module/kreationsAntragsDetails/3301"))
                .andExpect(status().isOk());
    }

    @Test
    void testkreationsAntragsDetailsNoAccessForOrganisator() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("orga"));

        assertThrows(java.lang.AssertionError.class,
                () -> {
                    mvc.perform(get("/module/kreationsAntragsDetails/3301"))
                            .andExpect(status().isOk());
                });
    }

    @Test
    void testkreationsAntragsDetailsNoAccessForStudent() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("student"));

        assertThrows(java.lang.AssertionError.class,
                () -> {
                    mvc.perform(get("/module/kreationsAntragsDetails/3301"))
                            .andExpect(status().isOk());
                });
    }

    @Test
    void testkreationsAntragsDetailsNoAccessIfNotLoggedIn() throws Exception {

        assertThrows(java.lang.AssertionError.class,
                () -> {
                    mvc.perform(get("/module/kreationsAntragsDetails/3301"))
                            .andExpect(status().isOk());
                });
    }

    @Test
    void testmodifikationsAntragsdetailsViewName() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("sekretariat"));

        mvc.perform(get("/module/modifikationsAntragsdetails/3301"))
                .andExpect(view().name("antragdetails"));
    }

    @Test
    void testmodifikationsAntragsdetailsAccessForAdministrator() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("sekretariat"));

        mvc.perform(get("/module/modifikationsAntragsdetails/3301"))
                .andExpect(status().isOk());
    }

    @Test
    void testmodifikationsAntragsdetailsNoAccessForOrganisator() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("orga"));

        assertThrows(java.lang.AssertionError.class,
                () -> {
                    mvc.perform(get("/module/modifikationsAntragsdetails/3301"))
                            .andExpect(status().isOk());
                });
    }

    @Test
    void testmodifikationsAntragsdetailsNoAccessForStudent() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("student"));

        assertThrows(java.lang.AssertionError.class,
                () -> {
                    mvc.perform(get("/module/modifikationsAntragsdetails/3301"))
                            .andExpect(status().isOk());
                });
    }

    @Test
    void testmodifikationsAntragsdetailssNoAccessIfNotLoggedIn() throws Exception {

        assertThrows(java.lang.AssertionError.class,
                () -> {
                    mvc.perform(get("/module/modifikationsAntragsdetails/3301"))
                            .andExpect(status().isOk());
                });
    }


}