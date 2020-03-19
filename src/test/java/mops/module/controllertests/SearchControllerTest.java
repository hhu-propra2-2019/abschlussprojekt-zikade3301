package mops.module.controllertests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerTest {

    @Autowired
    MockMvc mvc;

    final String expect = "searchresults";

    @Test
    void testSearchViewName() throws Exception {
        mvc.perform(get("/module/search?searchField="))
                .andExpect(view().name(expect));
    }

    @Test
    void testSearchStatus() throws Exception {
        mvc.perform(get("/module/search?searchField="))
                .andExpect(status().isOk());
    }

}
