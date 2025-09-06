package org.ezcode.codetest.domain.submission.dto;

import java.time.LocalDate;
import java.util.Set;

public record DailyCorrectCount(
    LocalDate date,
    long count,
    Set<Long> problemIds
) {  }
