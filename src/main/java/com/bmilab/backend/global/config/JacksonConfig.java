package com.bmilab.backend.global.config;

import com.bmilab.backend.global.utils.LocalTimeJsonHandler;
import java.time.LocalTime;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localTimeCustomizer() {
        return builder -> builder
                .serializerByType(LocalTime.class, new LocalTimeJsonHandler.Serializer())
                .deserializerByType(LocalTime.class, new LocalTimeJsonHandler.Deserializer());
    }
}
