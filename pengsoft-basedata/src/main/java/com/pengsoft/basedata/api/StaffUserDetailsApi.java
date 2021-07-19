package com.pengsoft.basedata.api;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.StaffUserDetails;
import com.pengsoft.basedata.service.StaffUserDetailsService;
import com.pengsoft.security.annotation.AuthorityChanged;
import com.pengsoft.security.annotation.Authorized;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link StaffUserDetails}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Authorized
@RestController
@RequestMapping("api/user-details")
public class StaffUserDetailsApi {

    @Inject
    private StaffUserDetailsService service;

    @AuthorityChanged
    @PostMapping("set-primary-job")
    public UserDetails setPrimaryJob(@RequestParam("id") final Job job) {
        return service.setPrimaryJob(job);
    }

}
