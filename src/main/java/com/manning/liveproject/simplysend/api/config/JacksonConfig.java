package com.manning.liveproject.simplysend.api.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.manning.liveproject.simplysend.api.util.SanitisedTextSerialiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * By default, the Spring Boot configuration will disable the following:
 *
 * <ul>
 * <li>MapperFeature.DEFAULT_VIEW_INCLUSION</li>
 * <li>DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES</li>
 * <li>SerializationFeature.WRITE_DATES_AS_TIMESTAMPS</li>
 * </ul>
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
                .dateFormat(StdDateFormat.getInstance())
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .serializers(new SanitisedTextSerialiser());
    }
}
