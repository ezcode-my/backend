package org.ezcode.codetest.application.submission.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.ezcode.codetest.application.submission.port.LockManager;
import org.ezcode.codetest.domain.submission.exception.CodeReviewException;
import org.ezcode.codetest.domain.submission.exception.code.CodeReviewExceptionCode;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CodeReviewLockAspect {

    private final LockManager lockManager;

    @Around("@annotation(org.ezcode.codetest.application.submission.aop.CodeReviewLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        String prefix = signature.getMethod().getAnnotation(CodeReviewLock.class).prefix();
        Object[] args = joinPoint.getArgs();

        Long problemId = null;
        Long userId = null;

        for (Object arg : args) {
            if (arg instanceof Long) {
                problemId = (Long)arg;
            } else if (arg instanceof AuthUser) {
                userId = ((AuthUser)arg).getId();
            }
        }

        if (problemId == null || userId == null) {
            throw new CodeReviewException(CodeReviewExceptionCode.REQUIRED_ARGS_NOT_FOUND);
        }

        boolean locked = lockManager.tryLock(prefix, userId, problemId);
        if (!locked) {
            throw new CodeReviewException(CodeReviewExceptionCode.ALREADY_REVIEWING);
        }

        try {
            return joinPoint.proceed();
        } finally {
            lockManager.releaseLock(prefix, userId, problemId);
        }
    }
}
