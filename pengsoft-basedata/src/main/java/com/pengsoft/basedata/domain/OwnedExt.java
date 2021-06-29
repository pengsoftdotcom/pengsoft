package com.pengsoft.basedata.domain;

import com.pengsoft.security.domain.Owned;

/**
 * Owned extension
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface OwnedExt extends Owned {

    /**
     * Returns the value of the entity's 'controlledBy' field.
     */
    String getControlledBy();

    /**
     * Set the value of the entity's 'controlledBy' field.
     *
     * @param controlledBy The owner({@link Department}) id of the entity.
     */
    void setControlledBy(String controlledBy);

    /**
     * Returns the value of the entity's 'belongsTo' field.
     */
    String getBelongsTo();

    /**
     * Set the value of the entity's 'belongsTo' field.
     *
     * @param belongsTo The owner({@link Organization}) id of the entity.
     */
    void setBelongsTo(String belongsTo);

}
