package com.example.java21.project;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

/**
 * Transforme des mesures en alertes lorsque l'indice de confort dépasse un seuil.
 */
public final class ComfortIndexProcessor extends SubmissionPublisher<CityAlert>
        implements Flow.Processor<WeatherRecord, CityAlert> {

    private final double threshold;
    private final String source;
    private Flow.Subscription subscription;

    public ComfortIndexProcessor(double threshold, String source, Executor executor) {
        super(executor, Flow.defaultBufferSize());
        this.threshold = threshold;
        this.source = Objects.requireNonNull(source);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = Objects.requireNonNull(subscription);
        subscription.request(1);
    }

    @Override
    public void onNext(WeatherRecord item) {
        var comfortIndex = computeComfortIndex(item);
        if (comfortIndex >= threshold) {
            submit(new CityAlert(item.city(), comfortIndex, Instant.now(), source));
        }
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        closeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        close();
    }

    private double computeComfortIndex(WeatherRecord weather) {
        double temperatureScore = 100 - Math.abs(22 - weather.temperatureCelsius()) * 3;
        double humidityScore = 100 - Math.abs(50 - weather.humidityPercentage()) * 1.5;
        return Math.max(0, (temperatureScore + humidityScore) / 2.0);
    }
}
