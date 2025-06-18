package org.ezcode.codetest.domain.ranking.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.ranking.model.enums.RankingType;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ranking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private RankingType type; // WEEKLY, MONTHLY, ALL_TIME

    private int rank;
    private int score;

    private LocalDate rankingDate; // 기준 날짜

    @Builder
    public Ranking(Long userId, RankingType type, int rank, int score, LocalDate rankingDate) {
        this.userId = userId;
        this.type = type;
        this.rank = rank;
        this.score = score;
        this.rankingDate = rankingDate;
    }
}
