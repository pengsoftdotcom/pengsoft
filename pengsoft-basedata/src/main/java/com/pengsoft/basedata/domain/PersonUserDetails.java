package com.pengsoft.basedata.domain;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pengsoft.basedata.json.OrganizationCollectionJsonSerializer;
import com.pengsoft.basedata.json.OrganizationJsonSerializer;
import com.pengsoft.security.domain.DefaultUserDetails;
import com.pengsoft.security.domain.Role;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

/**
 * The implementer of {@link UserDetails} for {@link Person}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
public class PersonUserDetails extends DefaultUserDetails {

    private static final long serialVersionUID = 6686517470713795533L;

    private Person person;

    @JsonSerialize(using = OrganizationCollectionJsonSerializer.class)
    private List<Organization> organizations;

    @JsonSerialize(using = OrganizationJsonSerializer.class)
    private Organization primaryOrganization;

    public PersonUserDetails(final Person person, final List<Organization> organizations, final List<Role> roles,
            final Role primaryRole, final List<GrantedAuthority> authorities) {
        super(person.getUser(), roles, primaryRole, authorities);
        this.person = person;
        this.organizations = organizations;
        if (primaryRole != null && primaryRole.getCode().equals(Role.ORG_ADMIN)
                && CollectionUtils.isNotEmpty(organizations)) {
            this.primaryOrganization = organizations.iterator().next();
        }
    }

}
