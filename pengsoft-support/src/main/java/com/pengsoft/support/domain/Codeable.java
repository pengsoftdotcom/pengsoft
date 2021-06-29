package com.pengsoft.support.domain;

/**
 * Any implementer of this interface must have 'code' field.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface Codeable {

    /**
     * Returns the value of the entity's 'code' field.
     */
    String getCode();

    /**
     * Set the value of the entity's 'code' field.
     *
     * @param code unique in the same associated entity
     */
    void setCode(String code);

}
