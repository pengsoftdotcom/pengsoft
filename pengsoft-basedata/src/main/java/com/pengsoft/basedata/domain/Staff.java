package com.pengsoft.basedata.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Staff
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "t_staff", indexes = {
        @Index(name = "i_staff_person_id_job_id", columnList = "person_id, job_id", unique = true) })
public class Staff extends OwnedExtEntity {

    private static final long serialVersionUID = -5537370953204778193L;

    @Valid
    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Person person = new Person();

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Job job;

    @Column(name = "`primary`")
    private boolean primary = true;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Department department;

}
