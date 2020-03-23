package mops.module.controllertests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static mops.module.controllertests.AuthenticationTokenGenerator.generateAuthenticationToken;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class ModulerstellungControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .apply(springSecurity())
                .build();
    }

    private final String expect = "modulerstellung";


    @Test
    void testModulerstellungViewName() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("orga"));

        mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1"))
                .andExpect(view().name(expect));
    }

    @Test
    void testModulerstellungAccessForOrganizers() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("orga"));

        mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1"))
                .andExpect(status().isOk());
    }

    @Test
    void testModulerstellungAccessForAdministrator() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("sekretariat"));

        mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1"))
                .andExpect(status().isOk());
    }

    @Test
    void testModulerstellungNoAccessIfNotLoggedIn() throws Exception {
        assertThrows(AssertionError.class,
                () -> {
                    mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1")).andExpect(view().name(expect));
                });
    }

    @Test
    void testModulerstellungNoAccessForStudents() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("studentin"));

        assertThrows(AssertionError.class,
                () -> {
                    mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1")).andExpect(view().name(expect));
                });
    }



    //TODO
    // Fehler wenn Parameter fehlt
    // Sekretariat hat Zugriff







}