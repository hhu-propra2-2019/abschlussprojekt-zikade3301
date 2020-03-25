package mops.module.controllertests;

import static mops.module.controllertests.AuthenticationTokenGenerator.generateAuthenticationToken;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import mops.module.generator.ModulFaker;
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
class ModulerstellungControllerTest {

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

    private final String expectGet = "modulerstellung";


    @Test
    void testModulerstellungViewName() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("orga"));

        mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1"))
                .andExpect(view().name(expectGet));
    }

    @Test
    void testGetModulerstellungAccessForOrganizers() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("orga"));

        mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetModulerstellungAccessForAdministrator() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("sekretariat"));

        mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetModulerstellungNoAccessIfNotLoggedIn() throws Exception {
        assertThrows(AssertionError.class,
                () -> {
                    mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1"))
                            .andExpect(view().name(expectGet));
                });
    }

    @Test
    void testGetModulerstellungNoAccessForStudents() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("studentin"));

        assertThrows(AssertionError.class,
                () -> {
                    mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1"))
                            .andExpect(view().name(expectGet));
                });
    }





// POST TESTS

    @Test
    void testPostModulerstellungAccessForOrganizers() throws Exception {
        SecurityContextHolder
                .getContext()
                .setAuthentication(generateAuthenticationToken("orga"));

        // TODO: ModulWrapper nutzen, sobald Elias gepusht hat
        //        Modul testmodul = ModulFaker.generateFakeModul();
        //        ModulWrapper testModulwrapper = new ModulWrapper(testmodul, testmodul.getVeranstaltungen, null, null);
        //        mvc.perform(post("/module/modulerstellung")
        //                .param("allParams", testModulwrapper))
        //                .andExpect(status().isOk());
    }




//
//    @Test
//    void testPostModulerstellungAccessForAdministrator() throws Exception {
//        SecurityContextHolder
//                .getContext()
//                .setAuthentication(generateAuthenticationToken("sekretariat"));
//
//        mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testPostModulerstellungNoAccessIfNotLoggedIn() throws Exception {
//        assertThrows(AssertionError.class,
//                () -> {
//                    mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1"))
//                            .andExpect(view().name(expectGet));
//                });
//    }
//
//    @Test
//    void testPostModulerstellungNoAccessForStudents() throws Exception {
//        SecurityContextHolder
//                .getContext()
//                .setAuthentication(generateAuthenticationToken("studentin"));
//
//        assertThrows(AssertionError.class,
//                () -> {
//                    mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1"))
//                            .andExpect(view().name(expectGet));
//                });
//    }



// verify(userService, times(1)).findById(user.getId());

//    @Test
//    public void test_create_user_success() throws Exception {
//        User user = new User("Arya Stark");
//        when(userService.exists(user)).thenReturn(false);
//        doNothing().when(userService).create(user);
//        mockMvc.perform(
//                post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(user)))
//                .andExpect(status().isCreated())
//                .andExpect(header().string("location", containsString("http://localhost/users/")));
//        verify(userService, times(1)).exists(user);
//        verify(userService, times(1)).create(user);
//        verifyNoMoreInteractions(userService);
//    }






// POST TESTS
//
//    @Test
//    void testModulerstellungViewName() throws Exception {
//        SecurityContextHolder
//                .getContext()
//                .setAuthentication(generateAuthenticationToken("orga"));
//
//        mvc.perform(get("/module/modulerstellung?veranstaltungsanzahl=1"))
//                .andExpect(view().name(expectGet));
//    }
//

    //TODO
    // Fehler wenn Parameter fehlt
    // ERSTELLUNG ANTRAG GEHT
    // VIEW NAME RICHTIG

}