package mops.module.repositories;

import java.util.Optional;

public class AntragDatabaseRepository implements AntragRepository {
    @Override
    public void updateModul(Antrag antrag) {
        this.save(antrag);
    }

    @Override
    public <S extends Antrag> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Antrag> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Antrag> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Antrag> findAll() {
        return null;
    }

    @Override
    public Iterable<Antrag> findAllById(Iterable<Long> longs) {
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
    public void delete(Antrag entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Antrag> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
