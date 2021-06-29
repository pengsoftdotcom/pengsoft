package com.pengsoft.support.service;

import java.util.Map;

import javax.validation.constraints.NotEmpty;

import com.pengsoft.support.domain.Sortable;

/**
 * The sort service interface.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface SortService {

    /**
     * sort
     *
     * @param sortInfo {key: entity, value: sequence}
     */
    void sort(@NotEmpty Map<? extends Sortable, Long> sortInfo);

}
