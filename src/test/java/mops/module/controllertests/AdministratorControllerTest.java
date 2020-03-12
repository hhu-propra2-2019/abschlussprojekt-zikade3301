package mops.module.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@AutoConfigureMockMvc
class AdministratorControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).alwaysDo(print()).apply(springSecurity()).build();
    }

    final String expect = "administrator";

//     TODO enmable tests if new role on keycloak available
//    @Test
//    void testAdministratorViewName() throws Exception {
//        mvc.perform(get("/module/administrator"))
//                .andExpect(view().name(expect));
//    }
//
//    @Test
//    void testAdministratorStatus() throws Exception {
//        mvc.perform(get("/module/administrator"))
//                .andExpect(status().isOk());
//    }

    // TODO write Test for keycloak if new role on keycloak available
    // TODO not OK when not logged in if new role on keycloak available
}