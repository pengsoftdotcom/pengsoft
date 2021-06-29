package com.pengsoft.basedata.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pengsoft.basedata.domain.Job;

/**
 * The {@link JsonSerializer} of {@link Job}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class JobJsonSerializer extends JsonSerializer<Job> {

    @Override
    public void serialize(final Job job, final JsonGenerator gen, final SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", job.getId());
        gen.writeStringField("name", job.getName());
        gen.writeObjectFieldStart("post");
        gen.writeStringField("id", job.getPost().getId());
        gen.writeStringField("name", job.getPost().getName());
        gen.writeEndObject();
        gen.writeObjectFieldStart("department");
        gen.writeStringField("id", job.getDepartment().getId());
        gen.writeStringField("name", job.getDepartment().getName());
        gen.writeEndObject();
        gen.writeObjectFieldStart("organization");
        gen.writeStringField("id", job.getDepartment().getOrganization().getId());
        gen.writeStringField("name", job.getDepartment().getOrganization().getName());
        gen.writeEndObject();
        gen.writeEndObject();
    }

}
