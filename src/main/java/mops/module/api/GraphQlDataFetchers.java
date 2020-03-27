package mops.module.api;

import graphql.schema.DataFetcher;
import mops.module.services.ModulService;
import org.springframework.stereotype.Component;

@Component
public class GraphQlDataFetchers {

    private ModulService modulService;

    public GraphQlDataFetchers(ModulService modulService) {
        this.modulService = modulService;
    }

    /**
     * Ruft das zur ID zugehörige Modul aus dem Repository ab und gibt es zurück.
     * @return GraphQL DataFetcher
     */
    public DataFetcher getModulByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String modulId = dataFetchingEnvironment.getArgument("id");
            return modulService.getModulById(Long.parseLong(modulId));
        };
    }

    public DataFetcher getAllModuleDataFetcher() {
        return dataFetchingEnvironment -> modulService.getAllModule();
    }
}