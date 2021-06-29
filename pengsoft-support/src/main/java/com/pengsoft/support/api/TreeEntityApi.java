package com.pengsoft.support.api;

import java.io.Serializable;
import java.util.List;

import com.pengsoft.support.domain.TreeEntity;
import com.pengsoft.support.service.TreeEntityService;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The web api of {@link TreeEntity}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class TreeEntityApi<F extends TreeEntityService<T, ID>, T extends TreeEntity<T, ID>, ID extends Serializable>
        extends EntityApi<F, T, ID> {

    @GetMapping("find-all-by-parent")
    public List<T> findAllByParent(final Predicate predicate, final Sort sort) {
        return getService().findAllByParent(predicate, sort);
    }

    @GetMapping("find-all-exclude-self-and-its-children-by-parent")
    public List<T> findAllExcludeSelfAndItsChildrenByParent(
            @RequestParam(value = "self.id", required = false) final T self, final Predicate predicate,
            final Sort sort) {
        return getService().findAllExcludeSelfAndItsChildrenByParent(self, predicate, sort);
    }

    @GetMapping("find-all-exclude-self-and-its-children")
    public List<T> findAllExcludeSelfAndItsChildren(@RequestParam(value = "self.id", required = false) final T self,
            final Predicate predicate, final Sort sort) {
        return getService().findAllExcludeSelfAndItsChildren(self, predicate, sort);
    }

}
