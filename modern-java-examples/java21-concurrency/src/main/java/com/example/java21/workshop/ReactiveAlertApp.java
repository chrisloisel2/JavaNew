package com.example.java21.workshop;

import com.example.java21.workshop.support.CityAlertSubscriber;
import com.example.java21.workshop.support.ComfortIndexProcessor;
import com.example.java21.workshop.support.WeatherRecord;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Travaux pratiques : application réactive avec tâches asynchrones et {@link java.util.concurrent.Flow}.
 */
public final class ReactiveAlertApp {

    public static void main(String[] args) throws Exception {
        new ReactiveAlertApp().run();
    }

    private void run() throws Exception {
        try (var publisherExecutor = Executors.newVirtualThreadPerTaskExecutor();
             var publisher = new SubmissionPublisher<WeatherRecord>(publisherExecutor, Flow.defaultBufferSize());
             var processor = new ComfortIndexProcessor(70, "comfort-pipeline", publisherExecutor)) {

            var subscriber = new CityAlertSubscriber();
            processor.subscribe(subscriber);
            publisher.subscribe(processor);

            var tasks = List.of(
                    emitSensor("Paris", publisher),
                    emitSensor("Lyon", publisher),
                    emitSensor("Bordeaux", publisher));

            CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new)).join();
            publisher.close();
            subscriber.awaitCompletion();
        }
    }

    private CompletableFuture<Void> emitSensor(String city, SubmissionPublisher<WeatherRecord> publisher) {
        var random = ThreadLocalRandom.current();
        return CompletableFuture.runAsync(() -> {
            for (int i = 0; i < 5; i++) {
                var record = new WeatherRecord(city, 16 + random.nextDouble(15), 35 + random.nextDouble(40), Instant.now());
                CompletableFuture.runAsync(() -> publisher.submit(record),
                        CompletableFuture.delayedExecutor(100 + random.nextInt(150), TimeUnit.MILLISECONDS)).join();
            }
        });
    }
}
