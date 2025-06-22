package org.ezcode.codetest.application.ranking.service;

import lombok.RequiredArgsConstructor;
import org.ezcode.codetest.domain.submission.repository.UserProblemResultRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserProblemResultRepository userProblemResultRepository;

    public int getTotalPoints(Long userId) {
        return userProblemResultRepository.sumScoreByUserId(userId).orElse(0);
    }

}
