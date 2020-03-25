package mops.module.controllertests;

import static mops.module.controllertests.AuthenticationTokenGenerator.generateAuthenticationToken;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
class ModulbeauftragterControllerTest {

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

    private final String expect = "modulbeauftragter";

    @Test
    void testModulbeauftragterViewName() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("orga"));

        mvc.perform(get("/module/modulbeauftragter"))
                .andExpect(view().name(expect));
    }

    @Test
    void testModulbeauftragterAccessForOrganizers() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("orga"));

        mvc.perform(get("/module/modulbeauftragter"))
                .andExpect(status().isOk());
    }

    @Test
    void testModulbeauftragterNoAccessForAdministrator() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("sekretariat"));

        mvc.perform(get("/module/modulbeauftragter"))
                .andExpect(status().isOk());
    }

    @Test
    void testModulbeauftragterNoAccessIfNotLoggedIn() throws Exception {
        assertThrows(java.lang.AssertionError.class,
                () -> {
                    mvc.perform(get("/module/modulbeauftragter")).andExpect(view().name(expect));
                });
    }

    @Test
    void testModulbeauftragterNoAccessForStudents() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("studentin"));

        assertThrows(java.lang.AssertionError.class,
                () -> {
                    mvc.perform(get("/module/modulbeauftragter")).andExpect(view().name(expect));
                });
    }
}