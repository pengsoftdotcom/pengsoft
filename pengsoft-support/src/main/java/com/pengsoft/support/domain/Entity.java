package com.pengsoft.support.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The Basic entity interface, all entities must implement it.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface Entity<ID extends Serializable> extends Serializable {

    /**
     * Get the value of the entity's 'id' field.
     *
     * @return id
     */
    ID getId();

    /**
     * Set the value of the entity's 'id' field.
     *
     * @param id id
     */
    void setId(ID id);

    /**
     * Get the value of the entity's 'createdAt' field.
     *
     * @return create datetime
     */
    LocalDateTime getCreatedAt();

    /**
     * Set the value of the entity's 'createdAt' field.
     *
     * @param createdAt create datetime
     */
    void setCreatedAt(LocalDateTime createdAt);

    /**
     * Get the value of the entity's 'updatedAt' field.
     *
     * @return update datetime
     */
    LocalDateTime getUpdatedAt();

    /**
     * Set the value of the entity's 'updatedAt' field.
     *
     * @param updatedAt update datetime
     */
    void setUpdatedAt(LocalDateTime updatedAt);

    /**
     * Get the value of the entity's 'version' field.
     *
     * @return version
     */
    long getVersion();

    /**
     * Set the value of the entity's 'version' field.
     *
     * @param version version
     */
    void setVersion(long version);

}
