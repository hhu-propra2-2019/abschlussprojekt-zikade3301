package mops.module.controllertests;

import static mops.module.controllertests.AuthenticationTokenGenerator.generateAuthenticationToken;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import mops.module.database.Antrag;
import mops.module.services.AntragService;
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
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdministratorControllerTest {

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

        Antrag antragToDelte = new Antrag();
        when(antragService.getAntragById(3302L)).thenReturn(antragToDelte);
    }

    private final String expect = "administrator";

    @Test
    void testAdministratorViewName() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("sekretariat"));

        mvc.perform(get("/module/administrator"))
                .andExpect(view().name(expect));
    }

    @Test
    void testAdministratorStatusLoggedIn() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("sekretariat"));

        mvc.perform(get("/module/administrator"))
                .andExpect(status().isOk());
    }

    @Test
    void testAdministratorNoAccessIfNotLoggedIn() {
        assertThrows(java.lang.AssertionError.class,
                () -> {
                    mvc.perform(get("/module/administrator")).andExpect(view().name(expect));
                });
    }

    @Test
    void testAdministratorNoAccessForStudents() {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("studentin"));

        assertThrows(java.lang.AssertionError.class,
                () -> {
                    mvc.perform(get("/module/administrator")).andExpect(view().name(expect));
                });
    }

    @Test
    void testAdministratorNoAccessForOrganisator() {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("orga"));

        assertThrows(java.lang.AssertionError.class,
                () -> {
                    mvc.perform(get("/module/administrator")).andExpect(view().name(expect));
                });
    }

    @Test
    void testDeleteAntragViewName() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("sekretariat"));

        mvc.perform(post("/module/deleteAntrag")
                .param("antragID", "3302"))
                .andExpect(view().name(expect));
    }

    @Test
    void testDeleteAntragAccessForAdmin() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("sekretariat"));

        mvc.perform(post("/module/deleteAntrag")
                .param("antragID", "3302"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteAntragNoAccessIfNotLoggedIn() {
        assertThrows(java.lang.AssertionError.class,
                () -> {
                    mvc.perform(post("/module/deleteAntrag")
                            .param("antragID", "3302"))
                            .andExpect(view().name(expect));
                });
    }

    @Test
    void testDeleteAntragNoAccessForStudents() {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("studentin"));

        assertThrows(java.lang.AssertionError.class,
                () -> {
                    mvc.perform(post("/module/deleteAntrag")
                            .param("antragID", "3302"))
                            .andExpect(view().name(expect));
                });
    }

    @Test
    void testDeleteAntragNoAccessForOrganisator() {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("orga"));

        assertThrows(java.lang.AssertionError.class,
                () -> {
                    mvc.perform(post("/module/deleteAntrag")
                            .param("antragID", "3302"))
                            .andExpect(view().name(expect));
                });
    }

    @Test
    void changeModulVisibilityControllerCallsChangeVisibility() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("sekretariat"));

        mvc.perform(post("/module/deleteAntrag")
                .param("antragID", "3302"));

        verify(antragService)
                .deleteAntrag(3302L);
    }
}
