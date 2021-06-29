package com.pengsoft.support.facade;

import java.io.Serializable;

import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.service.EntityService;

import org.springframework.validation.annotation.Validated;

/**
 * The facade interface of {@link Entity}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Validated
public interface EntityFacade<S extends EntityService<T, ID>, T extends Entity<ID>, ID extends Serializable>
        extends EntityService<T, ID> {

    S getService();

}
