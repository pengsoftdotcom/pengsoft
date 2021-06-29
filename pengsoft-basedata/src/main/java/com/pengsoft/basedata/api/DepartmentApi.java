package com.pengsoft.basedata.api;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.service.DepartmentService;
import com.pengsoft.support.api.TreeEntityApi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Department}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/department")
public class DepartmentApi extends TreeEntityApi<DepartmentService, Department, String> {

}
