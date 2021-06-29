package com.pengsoft.security.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pengsoft.support.domain.Codeable;
import com.pengsoft.support.domain.TreeEntityImpl;
import com.pengsoft.support.util.StringUtils;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Role
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "t_role", indexes = { @Index(name = "i_role_code", columnList = "code", unique = true),
        @Index(name = "i_role_name", columnList = "name", unique = true) })
public class Role extends TreeEntityImpl<Role> implements Codeable {

    /**
     * Role code: admin
     */
    public static final String ADMIN = "admin";

    /**
     * Role code: user
     */
    public static final String USER = "user";

    private static final long serialVersionUID = 8899680261707632937L;

    @NotBlank
    @Size(max = 255)
    private String code;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String remark;

    @OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE)
    private List<UserRole> userRoles = new ArrayList<>();

    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<RoleAuthority> roleAuthorities = new ArrayList<>();

    public Role(@NotBlank @Size(max = 255) final String code) {
        this.code = code;
        this.name = StringUtils.replace(code, StringUtils.UNDERLINE, StringUtils.SPACE);
    }

    public Role(@NotBlank @Size(max = 255) final String code, @NotBlank @Size(max = 255) final String name) {
        this.code = code;
        this.name = name;
    }

    public Role(final Role parent, @NotBlank @Size(max = 255) final String code) {
        setParent(parent);
        this.code = code;
        this.name = StringUtils.replace(code, StringUtils.UNDERLINE, StringUtils.SPACE);
    }

    public Role(final Role parent, @NotBlank @Size(max = 255) final String code,
            @NotBlank @Size(max = 255) final String name) {
        setParent(parent);
        this.code = code;
        this.name = name;
    }

}
