package com.pengsoft.basedata.domain;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Supplier
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "t_supplier_consumer", indexes = {
        @Index(name = "i_supplier_consumer", columnList = "supplier_id, consumer_id", unique = true) })
public class SupplierConsumer extends OwnedExtEntity {

    @Valid
    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Organization supplier = new Organization();

    @Valid
    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Organization consumer = new Organization();

}
