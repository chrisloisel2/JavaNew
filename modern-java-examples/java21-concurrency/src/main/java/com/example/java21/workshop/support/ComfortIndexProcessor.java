package com.example.java21.workshop.support;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

/**
 * {@link Flow.Processor} qui transforme un {@link WeatherRecord} en {@link CityAlert} si l'indice de confort
 * dépasse un seuil.
 */
public final class ComfortIndexProcessor extends SubmissionPublisher<CityAlert> implements Flow.Processor<WeatherRecord, CityAlert> {

    private final double comfortThreshold;
    private Flow.Subscription subscription;
    private final Executor executor;
    private final String name;

    public ComfortIndexProcessor(double comfortThreshold, String name, Executor executor) {
        super(executor, Flow.defaultBufferSize());
        this.comfortThreshold = comfortThreshold;
        this.executor = executor;
        this.name = name;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = Objects.requireNonNull(subscription);
        subscription.request(1);
        log("Abonnement au flux météo");
    }

    @Override
    public void onNext(WeatherRecord item) {
        CompletableFuture.runAsync(() -> {
            double comfortIndex = computeComfortIndex(item);
            if (comfortIndex >= comfortThreshold) {
                submit(new CityAlert(item.city(), comfortIndex, Instant.now()));
            }
            subscription.request(1);
        }, executor);
    }

    @Override
    public void onError(Throwable throwable) {
        log("Erreur dans le flux météo : " + throwable.getMessage());
        closeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        log("Flux météo terminé");
        close();
    }

    private double computeComfortIndex(WeatherRecord record) {
        double weatherScore = 100 - Math.abs(22 - record.temperatureCelsius()) * 2.5;
        double humidityScore = 100 - Math.abs(50 - record.humidityPercentage()) * 1.1;
        return Math.max(0, (weatherScore + humidityScore) / 2);
    }

    private void log(String message) {
        System.out.printf("[%s][%s] %s%n", Instant.now(), name, message);
    }
}
