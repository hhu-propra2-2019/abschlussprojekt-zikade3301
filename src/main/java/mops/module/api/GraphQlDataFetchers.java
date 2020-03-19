package mops.module.api;

import graphql.schema.DataFetcher;
import mops.module.repositories.AntragRepository;
import mops.module.repositories.ModulSnapshotRepository;
import mops.module.services.AntragService;
import mops.module.services.ModulService;
import org.springframework.stereotype.Component;

@Component
public class GraphQlDataFetchers {

    private ModulService modulService;
    private AntragService antragService;

    public GraphQlDataFetchers(AntragRepository antragRepository,
                               ModulSnapshotRepository modulSnapshotRepository) {
        modulService = new ModulService(antragRepository, modulSnapshotRepository);
        antragService = new AntragService(antragRepository, modulSnapshotRepository);
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
}