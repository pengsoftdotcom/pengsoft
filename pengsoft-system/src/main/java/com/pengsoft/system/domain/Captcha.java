package com.pengsoft.system.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pengsoft.security.domain.User;
import com.pengsoft.support.domain.Codeable;
import com.pengsoft.support.domain.EntityImpl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Captcha
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "t_captcha", indexes = { @Index(name = "i_captcha_user_id", columnList = "user_id, expiredAt") })
public class Captcha extends EntityImpl implements Codeable {

    private static final long serialVersionUID = 6091683647880038510L;

    @Size(max = 255)
    private String code;

    @NotNull
    private LocalDateTime expiredAt;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private User user;

}
