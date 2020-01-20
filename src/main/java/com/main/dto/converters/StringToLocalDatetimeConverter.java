package com.main.dto.converters;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalDatetimeConverter extends StdConverter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String value) {
        return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
