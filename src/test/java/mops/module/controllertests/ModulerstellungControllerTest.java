package mops.module.controllertests;

import static mops.module.controllertests.AuthenticationTokenGenerator.generateAuthenticationToken;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import mops.module.controller.ModulWrapper;
import mops.module.database.Modul;
import mops.module.generator.ModulFaker;
import mops.module.services.AntragService;
import mops.module.services.JsonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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


    private AntragService antragService;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .apply(springSecurity())
                .build();

        this.antragService = mock(AntragService.class);
    }

    private final String expectGet = "modulerstellung";
    private final String expectPost = "modulbeauftragter";


    @Test
    void testGetModulerstellungViewName() throws Exception {
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

//    @Test
//    void testPostModulerstellungViewName() throws Exception {
//        SecurityContextHolder
//                .getContext()
//                .setAuthentication(generateAuthenticationToken("orga"));
//
//        Modul testmodul = ModulFaker.generateFakeModul();
//        ModulWrapper testWrapper = new ModulWrapper(testmodul, null, null, null);
//        testWrapper.initPrefilled(6, 2);
//
//        mvc.perform(post("/module/modulerstellung")
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("modulId", "")
//                .content(JsonService.modulWrapperToJsonObject(testWrapper)))
//                .andExpect(view().name(expectPost));
//    }

//    @Test
//    void testPostModulerstellungAccessForOrganizers() throws Exception {
//        SecurityContextHolder
//                .getContext()
//                .setAuthentication(generateAuthenticationToken("orga"));
//
//        Modul testmodul = ModulFaker.generateFakeModul();
//        ModulWrapper testWrapper = new ModulWrapper(testmodul, null, null, null);
//        testWrapper.initPrefilled(6, 2);
//
//        mvc.perform(post("/module/modulerstellung")
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("modulId", "")
//                .content(JsonService.modulWrapperToJsonObject(testWrapper)))
//                .andExpect(status().isOk());
//    }

//    @Test
//    void testPostModulerstellungAccessForAdministrator() throws Exception {
//        SecurityContextHolder
//                .getContext()
//                .setAuthentication(generateAuthenticationToken("sekretariat"));
//
//        Modul testmodul = ModulFaker.generateFakeModul();
//        ModulWrapper testWrapper = new ModulWrapper(testmodul, null, null, null);
//        testWrapper.initPrefilled(6, 2);
//
//        mvc.perform(post("/module/modulerstellung")
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("modulId", "")
//                .content(JsonService.modulWrapperToJsonObject(testWrapper)))
//                .andExpect(status().isOk());
//    }

//    @Test
//    void testPostModulerstellungNoAccessIfNotLoggedIn() throws Exception {
//
//        assertThrows(AssertionError.class,
//                () -> {
//                    Modul testmodul = ModulFaker.generateFakeModul();
//                    ModulWrapper testWrapper = new ModulWrapper(testmodul, null, null, null);
//                    testWrapper.initPrefilled(6, 2);
//
//                    mvc.perform(post("/module/modulerstellung")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("modulId", "")
//                            .content(JsonService.modulWrapperToJsonObject(testWrapper)))
//                            .andExpect(status().isOk());
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
//                    Modul testmodul = ModulFaker.generateFakeModul();
//                    ModulWrapper testWrapper = new ModulWrapper(testmodul, null, null, null);
//                    testWrapper.initPrefilled(6, 2);
//
//                    mvc.perform(post("/module/modulerstellung")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("modulId", "")
//                            .content(JsonService.modulWrapperToJsonObject(testWrapper)))
//                            .andExpect(status().isOk());
//                });
//    }










//    @Test
//    void testPostModulerstellungCreateAntragIsCalled() throws Exception {
//        SecurityContextHolder
//                .getContext()
//                .setAuthentication(generateAuthenticationToken("orga"));
//
//        Modul testmodul = ModulFaker.generateFakeModul();
//        ModulWrapper testWrapper = new ModulWrapper(testmodul, null, null, null);
//        testWrapper.initPrefilled(6, 2);
//
//
//
//        mvc.perform(post("/module/modulerstellung")
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("modulId", "")
//                .content(JsonService.modulWrapperToJsonObject(testWrapper)));
//        verify(antragService).addModulCreationAntrag(testmodul, "");
//
//
//    }








    //TODO
    // Fehler wenn Parameter fehlt?
    // ERSTELLUNG ANTRAG GEHT
    // RENNWANZ KRAM DIREKT BESTÄTIGT


}