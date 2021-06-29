package com.pengsoft.support.service;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.pengsoft.support.domain.Enable;
import com.pengsoft.support.domain.Entity;

import org.springframework.stereotype.Service;

/**
 * The default implementer of {@link EnableService}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Service
public class EnableServiceImpl implements EnableService {

    @Inject
    private EntityManager entityManager;

    @Override
    public <T extends Enable> void enable(final T entity) {
        update(entity, true);
    }

    @Override
    public <T extends Enable> void disable(final T entity) {
        update(entity, false);
    }

    private <T extends Enable> void update(final T entity, final boolean enabled) {
        entity.setEnabled(enabled);
        final var ql = "update " + entity.getClass().getSimpleName() + " set enabled = ?1 where id = ?2";
        final var query = entityManager.createQuery(ql);
        int index = 1;
        query.setParameter(index++, enabled);
        query.setParameter(index, ((Entity<?>) entity).getId());
        query.executeUpdate();
    }

}
