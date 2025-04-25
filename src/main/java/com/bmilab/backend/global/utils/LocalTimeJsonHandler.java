package com.bmilab.backend.global.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeJsonHandler {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static class Serializer extends JsonSerializer<LocalTime> {
        @Override
        public void serialize(LocalTime value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
            generator.writeString(value.format(FORMATTER));
        }
    }

    public static class Deserializer extends JsonDeserializer<LocalTime> {
        @Override
        public LocalTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            return LocalTime.parse(parser.getText(), FORMATTER);
        }
    }
}
