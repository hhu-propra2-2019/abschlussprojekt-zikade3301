package mops.module.controllertests;

import static mops.module.controllertests.AuthenticationTokenGenerator.generateAuthenticationToken;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import mops.module.services.AntragService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
class ModulerstellungControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;


    private AntragService antragService;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .apply(springSecurity())
                .build();

        this.antragService = mock(AntragService.class);
    }

    private final String expectGet = "modulerstellung";
    private final String expectPost = "modulbeauftragter";


    @Test
    void testGetModulerstellungViewName() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("orga"));

        mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1"))
                .andExpect(view().name(expectGet));
    }

    @Test
    void testGetModulerstellungAccessForOrganizers() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("orga"));

        mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetModulerstellungAccessForAdministrator() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("sekretariat"));

        mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetModulerstellungNoAccessIfNotLoggedIn() throws Exception {
        assertThrows(AssertionError.class,
                () -> {
                    mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1"))
                            .andExpect(view().name(expectGet));
                });
    }

    @Test
    void testGetModulerstellungNoAccessForStudents() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("studentin"));

        assertThrows(AssertionError.class,
                () -> {
                    mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1"))
                            .andExpect(view().name(expectGet));
                });
    }


}