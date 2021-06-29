package com.pengsoft.basedata.facade;

import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.service.StaffService;
import com.pengsoft.support.facade.EntityFacade;

/**
 * The facade interface of {@link Staff}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface StaffFacade extends EntityFacade<StaffService, Staff, String>, StaffService {

}
