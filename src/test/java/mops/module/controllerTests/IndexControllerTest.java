package mops.module.controllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class IndexControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void testIndexViewName() throws Exception {
        final String expect = "index";
        mvc.perform(get("/module/"))
                .andExpect(view().name(expect));
    }

    @Test
    void testIndexStatus() throws Exception {
        mvc.perform(get("/module/"))
                .andExpect(status().isOk());
    }
}