package com.pengsoft.system.domain;

import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

import com.pengsoft.support.domain.EntityImpl;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * Message mapped superclass
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@MappedSuperclass
public class MessageMappedSuperclass extends EntityImpl {

    private static final long serialVersionUID = 4104623952728351613L;

    @Size(max = 255)
    private String subject;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String content;

    /**
     * {@link MessageType} string value, when multiple types are joined with ','
     */
    private String types;

}
