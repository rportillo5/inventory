package com.banderasmusic.rest.inventory.aspects;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RestControllerAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    Counter inventoryCreatedCounter = Metrics.counter("com.banderasmusic.rest.inventory.created");

    @Before("execution(public * com.banderasmusic.rest.inventory.controllers.*Controller.*(..))")
    public void generalAllMethodAspect(JoinPoint joinPoint) {
        logger.info("All Method Calls invoke this general aspect method: " + joinPoint);
        System.out.println("All Method Calls invoke this general aspect method: " + joinPoint);
    }

    @AfterReturning(value="execution(public * com.banderasmusic.rest.inventory.controllers.*Controller.createInventory(..))", returning="result")
    public void getCalledOnProductSave(JoinPoint joinPoint, Object result) {
        logger.info("All Save Method Calls invoke this general aspect method: " + result.toString());
        System.out.println("All SAVE Method Calls invoke this general aspect method: " + result.toString());
        inventoryCreatedCounter.increment();
    }
}
