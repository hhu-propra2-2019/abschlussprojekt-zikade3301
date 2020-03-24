package mops.module.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mops.module.database.Modul;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.aop.aspectj.annotation.LazySingletonAspectInstanceFactoryDecorator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class HibernateModuleSearch {

    @PersistenceContext
    private EntityManager entityManager;

    public void initIndex() throws InterruptedException {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();
    }

    @Transactional
    public List<Modul> search(String searchInput) {
        FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);

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

        return results;
    }

}
