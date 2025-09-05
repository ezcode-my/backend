package org.ezcode.codetest.domain.submission.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public record DailyCorrectCount(
    LocalDate date,
    int count,
    List<Long> problemIds
) {

    public DailyCorrectCount(java.sql.Date date, Set<Long> problemIds) {
        this(
            date.toLocalDate(),
            problemIds.size(),
            new ArrayList<>(problemIds)
        );
    }
}
