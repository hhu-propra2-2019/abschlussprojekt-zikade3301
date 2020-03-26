package mops.module.controllertests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import mops.module.database.Modul;
import mops.module.generator.ModulFaker;
import mops.module.services.ModulService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class PdfDownloadControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ModulService modulService;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void getPdfStatusTest() throws Exception {
        Modul testModul = ModulFaker.generateFakeModul();
        testModul.setId((long) 1);

        when(modulService.getAllSichtbareModule()).thenReturn(Arrays.asList(testModul));
        mvc.perform(get("/module/pdf"))
                .andExpect(status().isOk());
    }

    @Test
    public void getPdfContentTest() throws Exception {
        Modul testModul = ModulFaker.generateFakeModul();
        testModul.setId((long) 1);

        when(modulService.getAllSichtbareModule()).thenReturn(Arrays.asList(testModul));
        MvcResult result = mvc.perform(get("/module/pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isNotEmpty();
    }
}
