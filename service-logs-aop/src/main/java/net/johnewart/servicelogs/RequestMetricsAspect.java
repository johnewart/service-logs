package net.johnewart.servicelogs;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import net.johnewart.servicelogs.annotations.ServiceEndpoint;

@Aspect
public class RequestMetricsAspect {
    @Around("@annotation(endpoint)")
    public Object logAction(ProceedingJoinPoint pjp, ServiceEndpoint endpoint) throws Throwable {
        String endpointName = endpoint.endpointName();

        MetricsThreadLocal.get().setMethodName(endpointName);

        return pjp.proceed();
    }
}
