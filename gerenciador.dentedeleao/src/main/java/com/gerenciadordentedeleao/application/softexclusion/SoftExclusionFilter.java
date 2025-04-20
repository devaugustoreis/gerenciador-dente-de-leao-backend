package com.gerenciadordentedeleao.application.softexclusion;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

@Aspect
@Component
@Slf4j
public class SoftExclusionFilter {

    private static final String SOFT_EXCLUSION = "SOFT_EXCLUSION";

    private final PlatformTransactionManager transactionManager;

    private final CascadeFilterManager cascadeFilterManager;

    @Autowired
    public SoftExclusionFilter(PlatformTransactionManager transactionManager, CascadeFilterManager cascadeFilterManager) {
        this.transactionManager = transactionManager;
        this.cascadeFilterManager = cascadeFilterManager;
    }

    @Around("execution(* *.*(..)) && !execution(* *.save*(..)) && !execution(* *.delete*(..)) && target(org.springframework.data.repository.Repository)")
    public Object applyFilter(ProceedingJoinPoint joinPoint) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            var template = new TransactionTemplate(transactionManager);
            return template.execute(s -> applyFilterAndExecute(joinPoint));
        }
        return applyFilterAndExecute(joinPoint);
    }

    @SneakyThrows
    private Object applyFilterAndExecute(ProceedingJoinPoint joinPoint) {
        cascadeFilterManager.apply(SOFT_EXCLUSION);
        return joinPoint.proceed();
    }
}
