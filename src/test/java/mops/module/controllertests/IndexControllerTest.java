package mops.module.controllertests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class IndexControllerTest {

    @Autowired
    MockMvc mvc;

    final String expect = "index";

    @Test
    void testIndexViewName() throws Exception {
        mvc.perform(get("/module/"))
                .andExpect(view().name(expect));
    }

    @Test
    void testIndexStatus() throws Exception {
        mvc.perform(get("/module/"))
                .andExpect(status().isOk());
    }

    @Test
    void testModuldetails() throws Exception {
        mvc.perform(get("/module/moduldetails")
            .param("modulId","1")
        ).andExpect(model().attribute("modulId",equalTo("1")));
    }

    @Test
    void testSearchRouting() throws Exception {
        mvc.perform(get("/module/search")
             .param("searchField","someSearch")
        ).andExpect(view().name(expect));
    }

}