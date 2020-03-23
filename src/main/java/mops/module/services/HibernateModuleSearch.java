package mops.module.services;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mops.module.database.Modul;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
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
    public List<Modul> search(String searchinput) {
        FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);

        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
            .buildQueryBuilder().forEntity(Modul.class).get();
        org.apache.lucene.search.Query query = qb
            .keyword()
            .onFields("titelDeutsch", "titelEnglisch", "veranstaltungen.titel").boostedTo(10f)
            .matching(searchinput)
            .createQuery();

        javax.persistence.Query persistenceQuery =
            fullTextEntityManager.createFullTextQuery(query, Modul.class);

        List<Modul> results = persistenceQuery.getResultList();

        /*FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Modul.class).get();

        //TODO add all fields and boost individual fields in result (eg name)
        org.apache.lucene.search.Query query = queryBuilder
                .simpleQueryString()
                .onFields("titelDeutsch", "titelEnglisch", "veranstaltung.titel").boostedTo(10f)
                .andField("studiengang").boostedTo(3f)
                .andField("veranstaltung.titel").boostedTo(10f)
                .andField("veranstaltung.inhalte").boostedTo(5f)
                .andField("veranstaltung.lernergebnisse")
                .andField("veranstaltung.sprache")
                .matching(searchinput)
                .createQuery();

        org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, Modul.class);

        List<Modul> results = jpaQuery.getResultList();*/

        return results;
    }

}
