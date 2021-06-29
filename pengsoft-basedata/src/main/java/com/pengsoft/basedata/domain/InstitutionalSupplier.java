package com.pengsoft.basedata.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * Supplier
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Entity
@PrimaryKeyJoinColumn(name = "supplierId")
public class InstitutionalSupplier extends Supplier {

    @OneToOne
    private Organization organization;

}
