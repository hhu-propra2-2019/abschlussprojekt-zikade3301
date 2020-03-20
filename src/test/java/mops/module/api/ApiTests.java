package mops.module.api;

import static com.google.gson.JsonParser.parseString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import mops.module.database.Modul;
import mops.module.repositories.ModulSnapshotRepository;
import mops.module.services.JsonService;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class ApiTests {
    GraphQlProvider mockedGraphQlProvider;
    GraphQlProvider realGraphQlProvider;
    String testmodul1;
    String testmodul2;

    @Autowired
    private ModulSnapshotRepository modulSnapshotRepository;

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    /**
     * Konstruktor, der ein Testmodul erstellt und den Repositoryzugriff geeignet mockt.
     */
    public ApiTests() {
        testmodul1 = "{'titelDeutsch':'Betriebssysteme',"
                + "'veranstaltungen':[{'leistungspunkte':'5CP',"
                + "'beschreibung':{'inhalte':'Lorem'}}],"
                + "'modulkategorie':'MASTERARBEIT'}";
        testmodul2 = "{'datumErstellung':{'date':{'year':2011,'month':11,'day':11},"
                + "'time':{'hour':11,'minute':11,'second':11,'nano':0}}}";
        Modul modul1 = JsonService.jsonObjectToModul(testmodul1);
        Modul modul2 = JsonService.jsonObjectToModul(testmodul2);

        GraphQlDataFetchers graphQlDataFetchers = mock(GraphQlDataFetchers.class);
        when(graphQlDataFetchers.getModulByIdDataFetcher()).thenReturn(
                dataFetchingEnvironment -> modul1);
        when(graphQlDataFetchers.getAllModuleDataFetcher()).thenReturn(
                dataFetchingEnvironment -> Arrays.asList(modul1));
        mockedGraphQlProvider = new GraphQlProvider(graphQlDataFetchers);
        try {
            mockedGraphQlProvider.init();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .apply(springSecurity())
                .build();

        modulSnapshotRepository.deleteAll();
    }

    @Test
    public void graphQlModulByIdTest() {
        Map<String, Object> result = mockedGraphQlProvider.graphQL().execute(
                "{modulById(id: 1) {titelDeutsch veranstaltungen "
                        + "{leistungspunkte beschreibung {inhalte}} modulkategorie}}").getData();
        Modul resultModul = JsonService.jsonObjectToModul(result.get("modulById").toString());

        try {
            JSONAssert.assertEquals(testmodul1, JsonService.modulToJsonObject(resultModul), false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void graphQlAllModuleTest() {
        Map<String, Object> result = mockedGraphQlProvider.graphQL().execute(
                "{allModule {titelDeutsch veranstaltungen {leistungspunkte beschreibung {inhalte}} "
                        + "modulkategorie }}").getData();
        String resultString = result.get("allModule").toString();
        JsonElement jsonElement = parseString(resultString).getAsJsonArray().get(0);

        Modul resultModul = JsonService.jsonObjectToModul(jsonElement.toString());

        try {
            JSONAssert.assertEquals(testmodul1, JsonService.modulToJsonObject(resultModul), false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void graphQlRestTest() throws Exception {
        Modul modul1 = JsonService.jsonObjectToModul(testmodul1);
        modulSnapshotRepository.save(modul1);

        String query = String.format("{modulById(id:%d){titelDeutsch}}", modul1.getId());

        MvcResult mvcResult = mvc.perform(get("/module/api")
                .param("query", query))
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(content()
                        .json("{'data':{'modulById':{'titelDeutsch':'Betriebssysteme'}}}"));
    }

    @Test
    public void graphQlLocalDateTimeTest() throws Exception {
        Modul modul2 = JsonService.jsonObjectToModul(testmodul2);
        modulSnapshotRepository.save(modul2);

        MvcResult mvcResult = mvc.perform(get("/module/api")
                .param("query", "{allModule{datumErstellung}}"))
                .andReturn();

        mvcResult = mvc.perform(asyncDispatch(mvcResult)).andReturn();
        String resultString = mvcResult.getResponse().getContentAsString();

        String actualTimestamp = JsonParser.parseString(resultString)
                .getAsJsonObject()
                .getAsJsonObject("data")
                .getAsJsonArray("allModule")
                .get(0)
                .getAsJsonObject()
                .get("datumErstellung")
                .getAsString();

        LocalDateTime expectedDate = modul2.getDatumErstellung();
        assertThat(LocalDateTime.parse(actualTimestamp)).isEqualTo(expectedDate);
    }
}
