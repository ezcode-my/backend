package org.ezcode.codetest.domain.submission.dto;

import java.time.LocalDate;

public record DailyCorrectCount(
    LocalDate date,
    int count
) {
    public DailyCorrectCount(java.sql.Date date, int count) {
        this(date.toLocalDate(), count);
    }
}
