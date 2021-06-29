package com.pengsoft.basedata.domain;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.pengsoft.security.domain.Role;
import com.pengsoft.support.domain.EntityImpl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Many-to-many relationship between {@link Job} and {@link Role}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "t_job_role", indexes = { @Index(name = "i_job_role", columnList = "job_id, role_id", unique = true) })
public class JobRole extends EntityImpl {

    private static final long serialVersionUID = -2605345847395420443L;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Job job;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Role role;

    public JobRole(final Job job, final Role role) {
        this.job = job;
        this.role = role;
    }

}
