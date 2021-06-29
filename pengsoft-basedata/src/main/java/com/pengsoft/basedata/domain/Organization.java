package com.pengsoft.basedata.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pengsoft.support.domain.Codeable;
import com.pengsoft.system.domain.DictionaryItem;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Organization
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "t_organization", indexes = { @Index(name = "i_organization_code", columnList = "code", unique = true),
        @Index(name = "i_organization_name", columnList = "name", unique = true),
        @Index(name = "i_organization_short_name", columnList = "shortName") })
public class Organization extends OwnedExtTreeEntity<Organization> implements Codeable {

    private static final long serialVersionUID = -8823819150888810983L;

    @NotBlank
    @Size(max = 255)
    private String code;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String shortName;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private DictionaryItem category;

    @Valid
    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Person admin = new Person();

    @JsonIgnore
    @OneToMany(mappedBy = "organization", cascade = CascadeType.REMOVE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<Post> posts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "organization", cascade = CascadeType.REMOVE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<Department> departments = new ArrayList<>();

}
