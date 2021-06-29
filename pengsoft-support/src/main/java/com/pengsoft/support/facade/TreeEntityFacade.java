package com.pengsoft.support.facade;

import java.io.Serializable;

import com.pengsoft.support.domain.TreeEntity;
import com.pengsoft.support.service.TreeEntityService;

/**
 * The facade interface of {@link TreeEntity}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface TreeEntityFacade<S extends TreeEntityService<T, ID>, T extends TreeEntity<T, ID>, ID extends Serializable>
        extends TreeEntityService<T, ID>, EntityFacade<S, T, ID> {

}
