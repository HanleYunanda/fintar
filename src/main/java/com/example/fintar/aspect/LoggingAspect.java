package com.example.fintar.aspect;

import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Pointcut("execution(* com.example.fintar.service..*(..))")
  public void serviceMethods() {}

  @Around("serviceMethods()")
  public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().getName();
    String className = joinPoint.getTarget().getClass().getSimpleName();
    Object[] args = joinPoint.getArgs();

    logger.info(
        "Entering method: {}.{} with arguments: {}", className, methodName, Arrays.toString(args));

    long start = System.currentTimeMillis();
    try {
      Object result = joinPoint.proceed();
      long executionTime = System.currentTimeMillis() - start;
      logger.info(
          "Exiting method: {}.{}. Execution time: {} ms. Return value: {}",
          className,
          methodName,
          executionTime,
          result);
      return result;
    } catch (Throwable e) {
      long executionTime = System.currentTimeMillis() - start;
      logger.error(
          "Exception in method: {}.{}. Execution time: {} ms. Exception: {}",
          className,
          methodName,
          executionTime,
          e.getMessage());
      throw e;
    }
  }
}
