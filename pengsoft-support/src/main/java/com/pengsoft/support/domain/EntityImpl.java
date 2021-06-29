package com.pengsoft.support.domain;

import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.StringUtils;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

/**
 * The Default implementer of {@link Entity}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@MappedSuperclass
public class EntityImpl implements Entity<String> {

    private static final long serialVersionUID = 314634114783440828L;

    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Version
    private long version;

    @PrePersist
    public void preCreate() {
        final var now = DateUtils.currentDateTime();
        setCreatedAt(now);
        setUpdatedAt(now);
    }

    @PreUpdate
    public void preUpdate() {
        final var now = DateUtils.currentDateTime();
        setUpdatedAt(now);
    }

    @Override
    public String toString() {
        final var builder = new StringBuilder();
        builder.append(getClass().getSimpleName());
        if (this instanceof Codeable) {
            builder.append(StringUtils.GLOBAL_SEPARATOR).append(((Codeable) this).getCode());
        }
        builder.append(StringUtils.GLOBAL_SEPARATOR).append(getId());
        return builder.toString();
    }

}
