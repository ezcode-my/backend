package org.ezcode.codetest.domain.submission.dto;

import java.time.LocalDate;

public record DailyCorrectCount(
    LocalDate date,
    int count
) {
}
