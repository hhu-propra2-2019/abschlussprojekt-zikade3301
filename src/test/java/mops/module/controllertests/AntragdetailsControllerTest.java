package mops.module.controllertests;

import static mops.module.controllertests.AuthenticationTokenGenerator.generateAuthenticationToken;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    private final String expect = "antragdetails";



    //TODO - Wsh nicht korrekt so

    @Test
    void testAntragdetailsViewName() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("sekretariat"));

        Antrag neuerAntrag = new Antrag();
        neuerAntrag.setJsonModulAenderung("{'studiengang':'Informatik'}");

        when(antragService.getAntragById((long) 3301)).thenReturn(neuerAntrag);

        mvc.perform(get("/module/antragdetails/3301"))
                .andExpect(view().name(expect));
    }

    @Test
    void testAntragdetailsStatus() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("sekretariat"));

        Antrag neuerAntrag = new Antrag();
        neuerAntrag.setJsonModulAenderung("{'studiengang':'Informatik'}");

        when(antragService.getAntragById((long) 3301)).thenReturn(neuerAntrag);

        mvc.perform(get("/module/antragdetails/3301"))
                .andExpect(status().isOk());
    }


    //TODO -
    @Test
    void testAntragAnnehmen() {
    }
}