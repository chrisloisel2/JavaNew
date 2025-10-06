package com.example.java21.project;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Application réactive combinant {@link CompletableFuture} et {@link Flow} (Reactive Streams).
 */
public final class ReactiveWorkshop {

    public static void main(String[] args) throws Exception {
        new ReactiveWorkshop().run();
    }

    private void run() throws Exception {
        try (var publisherExecutor = Executors.newVirtualThreadPerTaskExecutor();
             var publisher = new SubmissionPublisher<WeatherRecord>(publisherExecutor, Flow.defaultBufferSize());
             var processor = new ComfortIndexProcessor(70, "comfort-pipeline", publisherExecutor)) {

            var subscriber = new CityAlertSubscriber();
            processor.subscribe(subscriber);
            publisher.subscribe(processor);

            var tasks = List.of(
                    emitSensor("sensor-paris", publisher),
                    emitSensor("sensor-lyon", publisher),
                    emitSensor("sensor-bordeaux", publisher));

            CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new)).join();
            publisher.close();

            subscriber.awaitCompletion();
        }
    }

    private CompletableFuture<Void> emitSensor(String sensorId, SubmissionPublisher<WeatherRecord> publisher) {
        var random = ThreadLocalRandom.current();
        return CompletableFuture.runAsync(() -> {
            for (int i = 0; i < 5; i++) {
                CompletableFuture.runAsync(() -> publisher.submit(new WeatherRecord(
                                sensorId.replace("sensor-", ""),
                                16 + random.nextDouble(15),
                                35 + random.nextDouble(40),
                                Instant.now())),
                        CompletableFuture.delayedExecutor(150 + random.nextInt(200), TimeUnit.MILLISECONDS))
                        .join();
            }
        });
    }
}
