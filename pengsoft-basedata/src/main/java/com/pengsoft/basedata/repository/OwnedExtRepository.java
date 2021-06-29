package com.pengsoft.basedata.repository;

import java.util.Collection;

import javax.persistence.QueryHint;

import com.pengsoft.basedata.domain.OwnedExt;
import com.pengsoft.security.repository.OwnedRepository;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * The repository interface of {@link OwnedExt} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@NoRepositoryBean
public interface OwnedExtRepository extends OwnedRepository {

    /**
     * Returns the count of entities with given ids and belongsTo.
     *
     * @param ids       The id collection
     * @param belongsTo The belongsTo
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    long countByIdInAndBelongsTo(Collection<String> ids, String belongsTo);

    /**
     * Returns the count of entities with given ids and controlledBy.
     *
     * @param ids          The entity's id
     * @param controlledBy The entity's controlledBy
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    long countByIdInAndControlledBy(Collection<String> ids, String controlledBy);

}
