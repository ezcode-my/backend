package org.ezcode.codetest.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.ezcode.codetest.domain.report.model.ReportTargetType;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportDomainService{

    public void validate(User reporter, Long targetId, ReportTargetType targetType) {
        if (targetType == ReportTargetType.USER && reporter.getId().equals(targetId)) {
            throw new IllegalArgumentException("자기 자신은 신고할 수 없습니다.");
        }
    }
}
