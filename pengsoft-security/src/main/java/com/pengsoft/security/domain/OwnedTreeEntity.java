package com.pengsoft.security.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.domain.TreeEntityImpl;
import com.pengsoft.support.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * {@link TreeEntityImpl} implements {@link Owned}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@MappedSuperclass
public class OwnedTreeEntity<T extends OwnedTreeEntity<T>> extends TreeEntityImpl<T> implements Owned {

    private static final long serialVersionUID = -3107782926731669275L;

    @Column(updatable = false)
    private String createdBy;

    private String updatedBy;

    @Override
    public void preCreate() {
        super.preCreate();
        final var userId = SecurityUtils.getUserId();
        if (StringUtils.isBlank(getCreatedBy())) {
            setCreatedBy(userId);
        }
        if (StringUtils.isBlank(getUpdatedBy())) {
            setUpdatedBy(userId);
        }
    }

    @Override
    public void preUpdate() {
        super.preUpdate();
        final var userId = SecurityUtils.getUserId();
        setUpdatedBy(userId);
    }

}
