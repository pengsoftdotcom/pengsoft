package com.pengsoft.security.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pengsoft.security.domain.Role;

/**
 * The {@link JsonSerializer} of {@link Role}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class RoleJsonSerializer extends JsonSerializer<Role> {

    @Override
    public void serialize(final Role role, final JsonGenerator gen, final SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", role.getId());
        gen.writeStringField("code", role.getCode());
        gen.writeStringField("name", role.getName());
        gen.writeEndObject();
    }

}
