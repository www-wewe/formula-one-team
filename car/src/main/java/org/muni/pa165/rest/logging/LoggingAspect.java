package org.muni.pa165.rest.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* org.muni.pa165.rest.*.*(..))")
    public void logMethodExecuted(JoinPoint joinPoint) {
        String endpointName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        logger.debug("{} called with {}", endpointName, args);
    }

    @AfterReturning(
            pointcut = "execution(* org.muni.pa165.rest.*.*(..))",
            returning = "result")
    public void afterReturningFromMethod(JoinPoint joinPoint, Object result) {
        String endpointName = joinPoint.getSignature().getName();
        logger.debug("{} returned {}", endpointName, result);
    }
}
