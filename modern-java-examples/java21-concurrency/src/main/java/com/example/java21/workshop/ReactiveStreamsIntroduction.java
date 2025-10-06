package com.example.java21.workshop;

import com.example.java21.workshop.support.WeatherRecord;

import java.time.Instant;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Introduction aux Reactive Streams via {@link Flow} et {@link SubmissionPublisher}.
 */
public final class ReactiveStreamsIntroduction {

    public static void main(String[] args) throws Exception {
        try (var publisher = new SubmissionPublisher<WeatherRecord>()) {
            publisher.subscribe(new LoggingWeatherSubscriber());

            for (int i = 0; i < 5; i++) {
                var record = new WeatherRecord("Paris", 15 + ThreadLocalRandom.current().nextDouble(10), 45 + ThreadLocalRandom.current().nextDouble(20), Instant.now());
                publisher.submit(record);
                TimeUnit.MILLISECONDS.sleep(150);
            }
        }
    }

    private static final class LoggingWeatherSubscriber implements Flow.Subscriber<WeatherRecord> {

        private Flow.Subscription subscription;

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            subscription.request(1);
            log("Abonnement reçu");
        }

        @Override
        public void onNext(WeatherRecord item) {
            log("Mesure reçue → %s %.1f°C / %.1f%%".formatted(item.city(), item.temperatureCelsius(), item.humidityPercentage()));
            subscription.request(1);
        }

        @Override
        public void onError(Throwable throwable) {
            log("Erreur : " + throwable.getMessage());
        }

        @Override
        public void onComplete() {
            log("Flux terminé");
        }

        private void log(String message) {
            System.out.printf("[%s][ReactiveIntro] %s%n", Instant.now(), message);
        }
    }
}
