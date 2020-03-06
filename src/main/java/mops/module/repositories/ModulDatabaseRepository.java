package mops.module.repositories;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import mops.module.database.Modul;

public class ModulDatabaseRepository implements ModulRepository {
    @Override
    public Antrag getCorrespondingAntrag(Modul modul) {
        String modul_id = modul.ID;
        Modul corresponding_modul = findByUUID(modul_id);

        return calculateDiffs(corresponding_modul, modul);
    }

    public static Antrag calculateDiffs(Modul altesModul, Modul neuesModul) {
        Antrag antrag = new Antrag();
        antrag.datum = LocalDateTime.now();
        antrag.ModulID = altesModul.ID;
        List<Aenderung> aenderungen = new ArrayList<Aenderung>();
        antrag.aenderungen = aenderungen;

        /*
        forEach Eigenschaft {
            if(altesModul.Eigenschaft != neuesModul.Eigenschaft) {
                aenderungen.Add(new Aenderung(Eigenschaft, neuesModul.Eigenschaft));
            }
        }
         */
        return antrag;
    }

    private Modul findByUUID(String uuid) {
        //...
        return null;
    }

    @Override
    public <S extends Modul> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Modul> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Modul> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Modul> findAll() {
        return null;
    }

    @Override
    public Iterable<Modul> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Modul entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Modul> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
