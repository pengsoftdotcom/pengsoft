package com.pengsoft.security.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.pengsoft.support.domain.EntityImpl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Many-to-many relationship between {@link User} and {@link Role}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "t_user_role", indexes = { @Index(name = "i_user_role", columnList = "user_id, role_id", unique = true) })
public class UserRole extends EntityImpl {

    private static final long serialVersionUID = 1105413409437052244L;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private User user;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Role role;

    /**
     * Whether the primary role.
     */
    @Column(name = "`primary`")
    private boolean primary;

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }

}
