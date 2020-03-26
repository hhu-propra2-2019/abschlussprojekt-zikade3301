package mops.module.controllertests;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import mops.module.services.SuchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class SearchControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    SuchService suchServiceMock;

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

    @Test
    void testSearchMethodIsCalled() throws Exception {
        mvc.perform(get("/module/search?searchField="));
        verify(suchServiceMock).search(any());
    }
}
