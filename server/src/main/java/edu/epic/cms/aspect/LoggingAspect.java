package edu.epic.cms.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Pointcut for all controller methods
    @Pointcut("execution(* edu.epic.cms.controller..*(..))")
    public void controllerMethods() {}

    // Before advice - logs before method execution
    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        
        logger.info("==> Entering: {}.{}()", className, methodName);
        if (args != null && args.length > 0) {
            logger.info("==> Arguments: {}", Arrays.toString(args));
        }
    }

    // After advice - logs after method execution (regardless of outcome)
    @After("controllerMethods()")
    public void logAfter(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        
        logger.info("<== Exiting: {}.{}()", className, methodName);
    }

    // AfterReturning advice - logs after successful method execution
    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        
        logger.info("<== {}.{}() returned successfully", className, methodName);
        if (result != null) {
            logger.info("<== Return value: {}", result);
        }
    }

    // AfterThrowing advice - logs when method throws exception
    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        
        logger.error("<== {}.{}() threw exception: {}", className, methodName, exception.getMessage());
        logger.error("<== Exception type: {}", exception.getClass().getName());
    }
}
