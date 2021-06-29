package com.pengsoft.basedata.api;

import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.facade.StaffFacade;
import com.pengsoft.support.api.EntityApi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Staff}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/staff")
public class StaffApi extends EntityApi<StaffFacade, Staff, String> {

}
