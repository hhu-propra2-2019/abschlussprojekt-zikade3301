package mops.module.services;

import mops.module.database.Modul;
import org.hibernate.search.indexes.interceptor.EntityIndexingInterceptor;
import org.hibernate.search.indexes.interceptor.IndexingOverride;

public class IndexWhenVisibleInterceptor implements EntityIndexingInterceptor<Modul> {

    @Override
    public IndexingOverride onAdd(Modul modul) {
        if (modul.getSichtbar() != null && modul.getSichtbar()) {
            return IndexingOverride.APPLY_DEFAULT;
        }
        return IndexingOverride.SKIP;
    }

    @Override
    public IndexingOverride onUpdate(Modul modul) {
        if (modul.getSichtbar() != null && modul.getSichtbar()) {
            return IndexingOverride.APPLY_DEFAULT;
        }
        return IndexingOverride.SKIP;
    }

    @Override
    public IndexingOverride onDelete(Modul modul) {
        return IndexingOverride.APPLY_DEFAULT;
    }

    @Override
    public IndexingOverride onCollectionUpdate(Modul modul) {
        return onUpdate(modul);
    }
}
