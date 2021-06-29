package com.pengsoft.system.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * {@link RegionWrapper} JSON serializer.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class RegionWrapperJsonSerializer extends JsonSerializer<RegionWrapper> {

    @Override
    public void serialize(final RegionWrapper wrapper, final JsonGenerator gen, final SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();
        gen.writeStringField("name", wrapper.getRegion().getName());
        gen.writeStringField("index", wrapper.getRegion().getIndex());
        gen.writeEndObject();
    }

}
