package com.example.java21.workshop.support;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

/**
 * Abonné chargé d'afficher les alertes de confort générées par un {@link SubmissionPublisher}.
 */
public final class CityAlertSubscriber implements Flow.Subscriber<CityAlert> {

    private final CountDownLatch completion = new CountDownLatch(1);
    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = Objects.requireNonNull(subscription);
        subscription.request(1);
        log("Abonné initialisé");
    }

    @Override
    public void onNext(CityAlert item) {
        log("Nouvelle alerte → %s".formatted(item.formatted()));
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        log("Erreur dans le flux : " + throwable.getMessage());
        completion.countDown();
    }

    @Override
    public void onComplete() {
        log("Flux terminé");
        completion.countDown();
    }

    public void awaitCompletion() throws InterruptedException {
        completion.await();
    }

    private void log(String message) {
        System.out.printf("[%s][CityAlertSubscriber] %s%n", Instant.now(), message);
    }
}
