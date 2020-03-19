package mops.module.api;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import mops.module.database.Modul;
import mops.module.services.JsonService;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class ApiTests {
    GraphQlProvider mockedGraphQlProvider;
    GraphQlProvider realGraphQlProvider;
    String testmodul;

    /**
     * Konstruktor, der ein Testmodul erstellt und den Repositoryzugriff geeignet mockt.
     */
    public ApiTests() {
        testmodul = "{'veranstaltungen':[{'leistungspunkte':'5CP',"
                + "'beschreibung':{'inhalte':'Lorem'}}],"
                + "'modulkategorie':'MASTERARBEIT'}";
        Modul modul = JsonService.jsonObjectToModul(testmodul);

        GraphQlDataFetchers graphQlDataFetchers = mock(GraphQlDataFetchers.class);
        when(graphQlDataFetchers.getModulByIdDataFetcher()).thenReturn(
                dataFetchingEnvironment -> modul);
        when(graphQlDataFetchers.getAllModuleDataFetcher()).thenReturn(
                dataFetchingEnvironment -> Arrays.asList(modul));
        mockedGraphQlProvider = new GraphQlProvider(graphQlDataFetchers);
        try {
            mockedGraphQlProvider.init();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void graphQlModulByIdTest() {
        Map<String, Object> result = mockedGraphQlProvider.graphQL().execute(
                "{modulById(id: 4) {veranstaltungen {leistungspunkte beschreibung {inhalte}} "
                        + "modulkategorie }}").getData();
        Modul resultModul = JsonService.jsonObjectToModul(result.get("modulById").toString());

        try {
            JSONAssert.assertEquals(testmodul, JsonService.modulToJsonObject(resultModul), false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
