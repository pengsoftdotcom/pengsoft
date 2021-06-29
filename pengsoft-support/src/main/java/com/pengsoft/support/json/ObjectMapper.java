package com.pengsoft.support.json;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.pengsoft.support.util.DateUtils;

/**
 * JSON tools for reading and writing
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class ObjectMapper extends com.fasterxml.jackson.databind.ObjectMapper {

    private static final long serialVersionUID = -7003160382732604741L;

    public ObjectMapper() {

        registerModule(new Jdk8Module());

        registerModule(new ParameterNamesModule());

        registerModule(new Hibernate5Module());

        registerModule(new TrimStringModule());

        final JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATETIME_PATTERN)));
        module.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_PATTERN)));
        module.addSerializer(LocalTime.class,
                new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_TIME_PATTERN)));
        module.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATETIME_PATTERN)));
        module.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_PATTERN)));
        module.addDeserializer(LocalTime.class,
                new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_TIME_PATTERN)));
        registerModule(module);

        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

}
