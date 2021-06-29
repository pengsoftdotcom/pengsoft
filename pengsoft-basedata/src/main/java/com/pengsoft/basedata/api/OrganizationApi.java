package com.pengsoft.basedata.api;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.facade.OrganizationFacade;
import com.pengsoft.support.api.TreeEntityApi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Organization}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/organization")
public class OrganizationApi extends TreeEntityApi<OrganizationFacade, Organization, String> {

}
