package com.manager.finance.metric;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
@ConditionalOnProperty(name = "metric.executionTime")
public class ExecutionTime {

    @Around("@annotation(TrackExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Object proceed = joinPoint.proceed();
        stopwatch.stop();
        String methodName = joinPoint.getSignature().toShortString();
        log.debug("{} was executed in {}/{} nano/milliseconds", methodName, stopwatch.elapsed(TimeUnit.NANOSECONDS), stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return proceed;
    }
}