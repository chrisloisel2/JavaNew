package com.example.java21;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Virtual threads permettent de lancer des milliers de tâches bloquantes.
 */
public class VirtualThreadDemo {

    public void runManyTasks() throws InterruptedException {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 10; i++) {
                int taskId = i;
                executor.submit(() -> {
                    Thread.sleep(Duration.ofMillis(200));
                    return "task-" + taskId;
                });
            }
        }
        // L'executor attend la fin des tâches lorsqu'il est fermé.
        TimeUnit.MILLISECONDS.sleep(100);
    }
}
