package com.bmilab.backend.global.utils;

import java.util.List;

public record ExcelRow(
        List<String> data
) {
    public static ExcelRow of(String... data) {
        return new ExcelRow(List.of(data));
    }
}
