package com.example.backend.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Configuration avancée des executeurs asynchrones
 * Démontre l'utilisation de différents types d'executeurs pour différents cas d'usage
 */
@Configuration
public class AsyncExecutorConfig implements AsyncConfigurer {

    /**
     * Executeur par défaut utilisant des Virtual Threads
     * Idéal pour les opérations I/O intensives (appels API, base de données, etc.)
     */
    @Override
    public Executor getAsyncExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    /**
     * Executeur pour les tâches CPU intensives
     * Utilise un pool limité de threads platform (traditionnels)
     */
    @Bean(name = "cpuIntensiveExecutor")
    public Executor cpuIntensiveExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // Nombre de coeurs disponibles
        int processors = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(processors);
        executor.setMaxPoolSize(processors * 2);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("cpu-intensive-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }

    /**
     * Executeur pour les opérations I/O avec Virtual Threads
     * Peut gérer des milliers de tâches concurrentes
     */
    @Bean(name = "ioIntensiveExecutor")
    public Executor ioIntensiveExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    /**
     * Executeur traditionnel pour compatibilité
     * Pool de threads plateforme classique
     */
    @Bean(name = "traditionalExecutor")
    public Executor traditionalExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("traditional-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    /**
     * Gestion des exceptions non capturées dans les méthodes async
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, _) -> {
            System.err.println("Exception async non capturée dans la méthode: " + method.getName());
            System.err.println("Message: " + throwable.getMessage());
            throwable.printStackTrace();
        };
    }
}
