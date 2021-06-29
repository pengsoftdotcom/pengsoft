package com.pengsoft.basedata.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.domain.OwnedTreeEntity;
import com.pengsoft.support.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * {@link OwnedTreeEntity} implements {@link OwnedExt}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@MappedSuperclass
public class OwnedExtTreeEntity<T extends OwnedTreeEntity<T>> extends OwnedTreeEntity<T> implements OwnedExt {

    private static final long serialVersionUID = -326799436860550542L;

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
