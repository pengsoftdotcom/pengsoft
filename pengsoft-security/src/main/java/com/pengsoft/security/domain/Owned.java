package com.pengsoft.security.domain;

/**
 * Any entity that implements this interface means that the entity has an owner.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface Owned {

    /**
     * Returns the value of the entity's 'createdBy' field.
     */
    String getCreatedBy();

    /**
     * Set the value of the entity's 'createdBy' field.
     *
     * @param createdBy The creator({@link User}) id of the entity.
     */
    void setCreatedBy(String createdBy);

    /**
     * Returns the value of the entity's 'updatedBy' field.
     */
    String getUpdatedBy();

    /**
     * Set the value of the entity's 'updatedBy' field.
     *
     * @param updatedBy The updater({@link User}) id of the entity.
     */
    void setUpdatedBy(String updatedBy);

}
