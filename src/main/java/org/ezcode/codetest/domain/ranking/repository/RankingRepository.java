package org.ezcode.codetest.domain.ranking.repository;

import org.ezcode.codetest.domain.ranking.model.entity.Ranking;
import org.ezcode.codetest.domain.ranking.model.enums.RankingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RankingRepository extends JpaRepository<Ranking, Long> {

    List<Ranking> findByTypeAndRankingDateOrderByRanksAsc(RankingType type, LocalDate date);
}
