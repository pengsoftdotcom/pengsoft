package com.pengsoft.basedata.api;

import java.util.List;
import java.util.Optional;

import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.JobRole;
import com.pengsoft.basedata.service.JobService;
import com.pengsoft.security.domain.Role;
import com.pengsoft.support.api.TreeEntityApi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Job}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/job")
public class JobApi extends TreeEntityApi<JobService, Job, String> {

    @PostMapping("grant-roles")
    public void grantRoles(@RequestParam("job.id") final Job job,
            @RequestParam(value = "role.id", defaultValue = "") final List<Role> roles) {
        getService().grantRoles(job, roles);
    }

    @GetMapping("find-all-job-roles-by-job")
    public List<JobRole> findAllJobRolesByJob(@RequestParam("id") final Job job) {
        return Optional.ofNullable(job).map(Job::getJobRoles).orElse(List.of());
    }

}
