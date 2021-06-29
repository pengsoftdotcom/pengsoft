package com.pengsoft.basedata.service;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.support.service.EntityService;

/**
 * The service interface of {@link Staff}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface StaffService extends EntityService<Staff, String> {

    /**
     * Set the primary job.
     *
     * @param person The {@link Person}.
     * @param job    The primary {@link Job}.
     */
    void setPrimaryJob(@NotNull Person person, @NotNull Job job);

    /**
     * Returns an {@link Optional} of a {@link Staff} with given person and primary
     * true.
     *
     * @param person {@link Staff}'s person
     */
    Optional<Staff> findOneByPersonAndPrimaryTrue(@NotNull Person person);

    /**
     * Returns all {@link Staff}s with given person.
     *
     * @param person {@link Staff}'s person
     */
    List<Staff> findAllByPerson(@NotNull Person person);

    /**
     * Returns all {@link Staff}s with given jobs
     *
     * @param jobs The {@link Staff}'s job
     */
    List<Staff> findAllByJobIn(@NotEmpty List<Job> jobs);

}
