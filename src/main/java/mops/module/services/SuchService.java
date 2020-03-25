package mops.module.services;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mops.module.database.Modul;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SuchService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Initializiert den Lucene Index für die volltextsuchenden Module.
     * Muss beim Start des Programms einmal ausgeführt werden, um die Daten die schon vorhanden sind zu indexieren.
     * Danach werden alle Einträge automatisch indexiert.
     */
    @Transactional
    public void initIndex() {
        FullTextEntityManager fullTextEntityManager;
        fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Die Volltextsuche wird auf alle relevanten Felder in den Modulen der Datenbank angewandt.
     * Die Ergebnisse sind grob nach relevanz sortiert: Treffer im Titel sind wichtiger als in der Beschreibung
     *
     * @param searchInput Ein String der einen oder mehrere Suchbegriffe beinhaltet.
     * @return  Gibt eine Liste der Module zurück, diese enhalten den gesuchten Begriff.
     */
    @Transactional
    public List<Modul> search(String searchInput) {
        FullTextEntityManager fullTextEntityManager;
        fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Modul.class).get();
        Query query = qb
                .simpleQueryString()
                .onFields("titelDeutsch", "titelEnglisch").boostedTo(10f)
                .andField("modulbeauftragte")
                .andField("studiengang")
                .andField("veranstaltungen.titel").boostedTo(9f)
                .andField("veranstaltungen.voraussetzungenTeilnahme")
                .andField("veranstaltungen.beschreibung.inhalte").boostedTo(8f)
                .andField("veranstaltungen.beschreibung.lernergebnisse")
                .andField("veranstaltungen.beschreibung.literatur").boostedTo(6f)
                .andField("veranstaltungen.beschreibung.verwendbarkeit")
                .andField("veranstaltungen.beschreibung.voraussetzungenBestehen")
                .andField("veranstaltungen.beschreibung.sprache")
                .matching(searchInput.toLowerCase() + "*")
                .createQuery();

        javax.persistence.Query persistenceQuery =
                fullTextEntityManager.createFullTextQuery(query, Modul.class);
        List<Modul> results = persistenceQuery.getResultList();

        removeNotVisibleModules(results);

        return results;
    }

    private void removeNotVisibleModules(List<Modul> results) {
        for (Modul result : results) {
            if (!result.getSichtbar()) {
                results.remove(result);
            }
        }
    }

}
