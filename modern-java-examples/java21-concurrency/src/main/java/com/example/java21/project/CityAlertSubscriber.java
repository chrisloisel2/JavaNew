package com.example.java21.project;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Consommateur réactif qui permet également de diagnostiquer les débits en affichant
 * le temps entre deux éléments.
 */
public final class CityAlertSubscriber implements Flow.Subscriber<CityAlert> {

    private final AtomicInteger received = new AtomicInteger();
    private final CountDownLatch completed = new CountDownLatch(1);
    private Flow.Subscription subscription;
    private Instant lastElementTime;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = Objects.requireNonNull(subscription);
        this.lastElementTime = Instant.now();
        subscription.request(1);
    }

    @Override
    public void onNext(CityAlert item) {
        var now = Instant.now();
        var delta = Duration.between(lastElementTime, now);
        System.out.printf("[%s] Alerte #%d reçue en %d ms : %s (indice=%.2f)%n",
                Thread.currentThread().getName(),
                received.incrementAndGet(),
                delta.toMillis(),
                item.city(),
                item.comfortIndex());
        lastElementTime = now;
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        System.err.println("Flux réactif interrompu : " + throwable.getMessage());
        completed.countDown();
    }

    @Override
    public void onComplete() {
        System.out.println("Flux réactif terminé.");
        completed.countDown();
    }

    public void awaitCompletion() throws InterruptedException {
        completed.await();
    }
}
