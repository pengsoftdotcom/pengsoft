package com.pengsoft.security.json;

import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.security.core.GrantedAuthority;

/**
 * The {@link JsonSerializer} of {@link GrantedAuthority} collection.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class GrantedAuthorityCollectionJsonSerializer extends JsonSerializer<Collection<GrantedAuthority>> {

    @Override
    public void serialize(final Collection<GrantedAuthority> authorities, final JsonGenerator gen,
            final SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        for (final GrantedAuthority authority : authorities) {
            gen.writeString(authority.getAuthority());
        }
        gen.writeEndArray();
    }

}
