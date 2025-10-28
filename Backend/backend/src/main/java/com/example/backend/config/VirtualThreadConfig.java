package com.example.backend.config;

import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;

import java.util.concurrent.Executors;

/**
 * Configuration des Virtual Threads (Project Loom) pour Java 21+
 * Les Virtual Threads permettent de g\u00e9rer des millions de threads concurrents avec peu de ressources
 */
@Configuration
public class VirtualThreadConfig {

    /**
     * Configure Tomcat pour utiliser les Virtual Threads
     * Améliore considérablement les performances pour les applications I/O-bound
     */
    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> {
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        };
    }

    /**
     * Configure l'ex\u00e9cuteur de t\u00e2ches asynchrones avec des Virtual Threads
     * Remplace le pool de threads traditionnel par des Virtual Threads
     */
    @Bean(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)
    public AsyncTaskExecutor asyncTaskExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }
}
