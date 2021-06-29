package com.pengsoft.basedata.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Job
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "t_job", indexes = {
        @Index(name = "i_job_department_id", columnList = "department_id, name", unique = true), })
public class Job extends OwnedExtTreeEntity<Job> {

    private static final long serialVersionUID = 2756342891165187326L;

    @NotBlank
    @Size(max = 255)
    private String name;

    private boolean departmentChief;

    private boolean organizationChief;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Post post;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Department department;

    @JsonIgnore
    @OneToMany(mappedBy = "job", cascade = CascadeType.REMOVE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<Staff> staffs = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "job", cascade = CascadeType.REMOVE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<JobRole> jobRoles = new ArrayList<>();

}
