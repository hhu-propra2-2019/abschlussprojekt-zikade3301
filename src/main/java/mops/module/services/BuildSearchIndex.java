package mops.module.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//based on https://github.com/netgloo/spring-boot-samples/blob/master/spring-boot-hibernate-search
@Component
public class BuildSearchIndex implements ApplicationListener<ApplicationReadyEvent> {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Initializiert den Lucene Index, der zur Volltextsuche auf den Modulen benötigt wird.
     * Muss beim Start des Programms einmal ausgeführt werden, um die Daten,
     * die schon vorhanden sind, zu indexieren.
     * Danach werden alle Einträge, welche mit Hibernate erzeugt werden, automatisch indexiert.
     */
    @Transactional
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        FullTextEntityManager fullTextEntityManager;
        fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            System.err.println("An error occurred trying to build the search index: ");
            e.printStackTrace();
        }
    }
}
