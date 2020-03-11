package mops.module.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import org.springframework.security.core.context.SecurityContextHolder;

import static mops.module.controllerTests.AuthenticationTokenGenerator.generateAuthenticationToken;

@SpringBootTest
@AutoConfigureMockMvc
class ModuleControllerTest {

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

        mvc.perform(get("/module/meineModule"))
                .andExpect(view().name("module"))
                .andExpect(status().isOk())
        ;
    }
}