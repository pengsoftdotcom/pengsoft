package com.pengsoft.security.json;

import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pengsoft.security.domain.Role;

/**
 * The {@link JsonSerializer} of {@link Role} collection.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class RoleCollectionJsonSerializer extends JsonSerializer<Collection<Role>> {

    private final RoleJsonSerializer roleJsonSerializer = new RoleJsonSerializer();

    @Override
    public void serialize(final Collection<Role> roles, final JsonGenerator gen, final SerializerProvider serializers)
            throws IOException {
        gen.writeStartArray();
        for (final Role role : roles) {
            roleJsonSerializer.serialize(role, gen, serializers);
        }
        gen.writeEndArray();
    }

}
