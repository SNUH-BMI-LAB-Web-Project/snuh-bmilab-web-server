package com.bmilab.backend.global.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LocalTimeConverter implements Converter<String, LocalTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");


    @Override
    public LocalTime convert(String source) {
        return LocalTime.parse(source, FORMATTER);
    }
}
