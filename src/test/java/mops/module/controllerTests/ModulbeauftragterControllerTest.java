package mops.module.controllerTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static mops.module.controllerTests.AuthenticationTokenGenerator.generateAuthenticationToken;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest
@AutoConfigureMockMvc
class ModulbeauftragterControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).alwaysDo(print()).apply(springSecurity()).build();
    }

    @Test
    void module() throws Exception{
        SecurityContextHolder.getContext().setAuthentication(generateAuthenticationToken( "orga"));

        mvc.perform(get("/module/modulbeauftragter"))
                .andExpect(view().name("modulbeauftragter"))
                .andExpect(status().isOk())
        ;
    }

    // TODO write Test for keycloak
}