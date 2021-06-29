package com.pengsoft.security.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import com.pengsoft.security.domain.Role;
import com.pengsoft.security.domain.User;
import com.pengsoft.security.domain.UserRole;
import com.pengsoft.security.repository.UserRepository;
import com.pengsoft.security.repository.UserRoleRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.StringUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link UserService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class UserServiceImpl extends EntityServiceImpl<UserRepository, User, String> implements UserService {

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRoleRepository userRoleRepository;

    @Inject
    private Validator validator;

    @Override
    public User saveWithoutValidation(User target) {
        if (StringUtils.isBlank(target.getId())) {
            findOneByUsername(target.getUsername()).ifPresent(source -> usernameAlreadyExists(source, target));
            findOneByMobile(target.getUsername()).ifPresent(source -> usernameAlreadyExists(source, target));
            findOneByEmail(target.getUsername()).ifPresent(source -> usernameAlreadyExists(source, target));
            findOneByMpOpenId(target.getUsername()).ifPresent(source -> usernameAlreadyExists(source, target));
            target.setPassword(passwordEncoder.encode(target.getPassword()));
        }
        return super.save(target);
    }

    @Override
    public User save(final User user) {
        if (StringUtils.isBlank(user.getId())) {
            final var constraintViolations = validator.validate(user, User.Create.class);
            if (CollectionUtils.isNotEmpty(constraintViolations)) {
                throw new ConstraintViolationException(constraintViolations);
            }

        }
        return super.save(user);
    }

    private void usernameAlreadyExists(final User source, final User target) {
        if (EntityUtils.ne(source, target)) {
            throw getExceptions().constraintViolated("username", "exists", target.getUsername());
        }
    }

    @Override
    public void changePassword(final String id, final String oldPassword, final String newPassword) {
        final var user = findOne(id).orElseThrow(() -> new IllegalArgumentException(
                "the entity with given id has been deleted or the given id is invalid."));
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            resetPassword(id, newPassword);
        } else {
            throw getExceptions().constraintViolated("oldPassword", "WrongPassword", oldPassword);
        }
    }

    @Override
    public void resetPassword(final String id, final String password) {
        if (findOne(id).isEmpty()) {
            throw getExceptions().entityNotFound(id);
        }
        getRepository().resetPassword(id, passwordEncoder.encode(password));
    }

    @Override
    public void grantRoles(final User user, final List<Role> roles) {
        final var source = user.getUserRoles();
        final var target = roles.stream().map(role -> new UserRole(user, role)).collect(Collectors.toList());
        final var deleted = source.stream()
                .filter(s -> target.stream().noneMatch(
                        t -> EntityUtils.eq(s.getUser(), t.getUser()) && EntityUtils.eq(s.getRole(), t.getRole())))
                .collect(Collectors.toList());
        userRoleRepository.deleteAll(deleted);
        source.removeAll(deleted);
        final var created = target.stream()
                .filter(t -> source.stream().noneMatch(
                        s -> EntityUtils.eq(t.getUser(), s.getUser()) && EntityUtils.eq(t.getRole(), s.getRole())))
                .collect(Collectors.toList());
        userRoleRepository.saveAll(created);
        source.addAll(created);
        super.save(user);
        if (!source.isEmpty() && source.stream().noneMatch(UserRole::isPrimary)) {
            setPrimaryRole(user, source.get(0).getRole());
        }
    }

    @Override
    public void setPrimaryRole(final User user, final Role role) {
        user.getUserRoles().forEach(userRole -> {
            userRole.setPrimary(EntityUtils.eq(userRole.getRole(), role));
            userRoleRepository.save(userRole);
        });
        super.save(user);
    }

    @Override
    public void signInSuccess(final String username) {
        final var user = findOneByUsername(username).orElseThrow(() -> getExceptions().entityNotFound(username));
        user.setSignedInAt(DateUtils.currentDateTime());
        user.setSignInFailureCount(0L);
        save(user);
    }

    @Override
    public void signInFailure(final String username, final int allowSignInFailure) {
        final var optional = findOneByUsername(username);
        if (optional.isPresent()) {
            final var user = optional.get();
            user.setSignInFailureCount(user.getSignInFailureCount() + 1);
            if (user.getSignInFailureCount() >= allowSignInFailure) {
                user.setEnabled(false);
            }
            save(user);
        }
    }

    @Override
    public Optional<User> findOneByUsername(final String username) {
        return getRepository().findOneByUsername(username);
    }

    @Override
    public Optional<User> findOneByMobile(final String mobile) {
        return getRepository().findOneByMobile(mobile);
    }

    @Override
    public Optional<User> findOneByEmail(final String email) {
        return getRepository().findOneByEmail(email);
    }

    @Override
    public Optional<User> findOneByMpOpenId(final String mpOpenId) {
        return getRepository().findOneByMpOpenId(mpOpenId);
    }

}
