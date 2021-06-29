package com.pengsoft.system.domain;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.pengsoft.support.domain.Codeable;
import com.pengsoft.support.domain.TreeEntityImpl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;

/**
 * The Administrative division.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "t_region", indexes = { @Index(name = "i_region_code", columnList = "code", unique = true),
        @Index(name = "i_region_name", columnList = "name"), @Index(name = "i_region_index", columnList = "index") })
public class Region extends TreeEntityImpl<Region> implements Codeable {

    private static final long serialVersionUID = -3810895754139198521L;

    @NotBlank
    @Size(max = 255)
    private String code;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String shortName;

    @Size(max = 255)
    private String index;

    @Size(max = 255)
    private String remark;

}
