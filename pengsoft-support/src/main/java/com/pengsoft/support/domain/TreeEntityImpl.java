package com.pengsoft.support.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Tree entity
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@MappedSuperclass
public class TreeEntityImpl<T extends TreeEntityImpl<T>> extends EntityImpl implements TreeEntity<T, String> {

    private static final long serialVersionUID = 6320599185190171935L;

    /**
     * Parent node.
     */
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private T parent;

    /**
     * All parent node IDs.
     */
    @Size(max = 2000)
    private String parentIds = "";

    /**
     * Child nodes.
     */
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<T> children = new ArrayList<>();

    /**
     * Whether it is a leaf node.
     */
    private boolean leaf = true;

    /**
     * The depth of the tree, the default value is 1.
     */
    private long depth = 1;

}
