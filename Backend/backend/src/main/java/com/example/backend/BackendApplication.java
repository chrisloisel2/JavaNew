package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Application principale avec support complet du multithreading moderne Java
 *
 * Fonctionnalités activées:
 * - @EnableAsync: Support des méthodes asynchrones avec @Async
 * - Virtual Threads: Configuration dans VirtualThreadConfig
 * - Structured Concurrency: StructuredTaskScope (Java 21+)
 * - Reactive Programming: Project Reactor
 */
@SpringBootApplication
@EnableAsync
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
