package com.pengsoft.basedata.facade;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.service.OrganizationService;
import com.pengsoft.support.facade.TreeEntityFacade;

/**
 * The facade interface of {@link Organization}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface OrganizationFacade
        extends TreeEntityFacade<OrganizationService, Organization, String>, OrganizationService {

}
