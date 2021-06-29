package com.pengsoft.security.repository;

import java.util.Collection;

import javax.persistence.QueryHint;

import com.pengsoft.security.domain.Owned;

import org.springframework.data.jpa.repository.QueryHints;

/**
 * The repository interface of {@link Owned} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface OwnedRepository {

    /**
     * Returns the count of entities with given ids and createdBy
     *
     * @param ids       The entity's id
     * @param createdBy The entity's createdBy
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    long countByIdInAndCreatedBy(Collection<String> ids, String createdBy);

}
