package tn.esprit.eventsproject.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PerformanceAspect {

    private final MeterRegistry meterRegistry;

    public PerformanceAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Around("execution(* tn.esprit.eventsproject.services.*.*(..))")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().getName();
        String className = pjp.getTarget().getClass().getSimpleName();

        // Compter les appels de méthode
        Counter.builder("method.calls")
                .tags("method", methodName, "class", className)
                .register(meterRegistry)
                .increment();

        // Mesurer le temps d'exécution
        long start = System.currentTimeMillis();
        try {
            return pjp.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;

            // Enregistrer le temps d'exécution
            Timer.builder("method.execution.duration")
                    .tags("method", methodName, "class", className)
                    .register(meterRegistry)
                    .record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);

            log.info("{}.{}() executed in {} ms", className, methodName, duration);
        }
    }
}