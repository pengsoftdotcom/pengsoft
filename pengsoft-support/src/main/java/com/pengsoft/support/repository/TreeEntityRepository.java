package com.pengsoft.support.repository;

import java.io.Serializable;
import java.util.List;

import javax.persistence.QueryHint;

import com.pengsoft.support.domain.TreeEntity;
import com.pengsoft.support.domain.TreeEntityImpl;
import com.querydsl.core.types.dsl.EntityPathBase;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * The repository interface of {@link TreeEntityImpl} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@NoRepositoryBean
public interface TreeEntityRepository<Q extends EntityPathBase<T>, T extends TreeEntity<T, ID>, ID extends Serializable>
        extends EntityRepository<Q, T, ID> {

    /**
     * Returns all entities that their 'parentIds' starts with the given value.
     *
     * @param parentIds The entity's parentIds
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<T> findAllByParentIdsStartsWith(String parentIds);

}
