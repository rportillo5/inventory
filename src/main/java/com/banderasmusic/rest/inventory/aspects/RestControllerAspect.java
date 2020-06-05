package com.banderasmusic.ecommerceproductapi.aspects;

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
    Counter productCreatedCounter = Metrics.counter("com.banderasmusic.product.created");

    //@Before("execution(* com.banderasmusic.ecommerceproductapi.controller.*.*(..))")
    @Before("execution(public * com.banderasmusic.ecommerceproductapi.controller.*Controller.*(..))")
    public void generalAllMethodAspect(JoinPoint joinPoint) {
        logger.info("All Method Calls invoke this general aspect method: " + joinPoint);
        System.out.println("All Method Calls invoke this general aspect method: " + joinPoint);
    }

    @AfterReturning(value="execution(public * com.banderasmusic.ecommerceproductapi.controller.*Controller.createProduct(..))", returning="result")
    public void getCalledOnProductSave(JoinPoint joinPoint, Object result) {
        logger.info("All Save Method Calls invoke this general aspect method: " + result.toString());
        System.out.println("All SAVE Method Calls invoke this general aspect method: " + result.toString());
        productCreatedCounter.increment();
    }
}
