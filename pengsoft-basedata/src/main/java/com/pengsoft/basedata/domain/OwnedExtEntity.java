package com.pengsoft.basedata.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.domain.OwnedEntity;
import com.pengsoft.support.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * {@link OwnedEntity} implements {@link OwnedExt}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@MappedSuperclass
public class OwnedExtEntity extends OwnedEntity implements OwnedExt {

    private static final long serialVersionUID = 7604551680091764860L;

    @Column(updatable = false)
    private String controlledBy;

    @Column(updatable = false)
    private String belongsTo;

    @Override
    public void preCreate() {
        super.preCreate();
        if (StringUtils.isBlank(getControlledBy())) {
            setControlledBy(SecurityUtilsExt.getDepartmentId());
        }
        if (StringUtils.isBlank(getBelongsTo())) {
            setBelongsTo(SecurityUtilsExt.getOrganizationId());
        }
    }

}
