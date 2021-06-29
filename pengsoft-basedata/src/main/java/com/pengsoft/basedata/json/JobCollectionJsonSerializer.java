package com.pengsoft.basedata.json;

import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pengsoft.basedata.domain.Job;

/**
 * The {@link JsonSerializer} of {@link Job} collection.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class JobCollectionJsonSerializer extends JsonSerializer<Collection<Job>> {

    private final JobJsonSerializer jobSerializer = new JobJsonSerializer();

    @Override
    public void serialize(final Collection<Job> jobs, final JsonGenerator gen, final SerializerProvider serializers)
            throws IOException {
        gen.writeStartArray();
        for (final Job job : jobs) {
            jobSerializer.serialize(job, gen, serializers);
        }
        gen.writeEndArray();
    }

}
