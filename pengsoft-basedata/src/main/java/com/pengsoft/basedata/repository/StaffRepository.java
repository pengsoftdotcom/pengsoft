package com.pengsoft.basedata.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.QStaff;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.support.repository.EntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link Staff} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface StaffRepository extends EntityRepository<QStaff, Staff, String>, OwnedExtRepository {

    @Override
    default void customize(final QuerydslBindings bindings, final QStaff root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.person.name).first(StringPath::contains);
        bindings.bind(root.person.nickname).first(StringPath::contains);
        bindings.bind(root.person.mobile).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link Staff} with given person and job.
     *
     * @param person {@link Staff}'s person
     * @param job    {@link Staff}'s job
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Staff> findOneByPersonAndJob(@NotNull Person person, @NotNull Job job);

    /**
     * Returns an {@link Optional} of a {@link Staff} with given person id and
     * primary true.
     *
     * @param personId The id of {@link Staff}'s person
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Staff> findOneByPersonIdAndPrimaryTrue(@NotBlank String personId);

    /**
     * Returns all {@link Staff}s with given person id.
     *
     * @param personId The id of {@link Staff}'s person
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<Staff> findAllByPersonId(@NotBlank String personId);

    /**
     * Returns all {@link Staff}s with given job ids
     *
     * @param jobIds The id of {@link Staff}'s job
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<Staff> findAllByJobIdIn(@NotEmpty List<String> jobIds);

}
