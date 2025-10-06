package com.example.java21.workshop;

import com.example.java21.workshop.support.CityReport;
import com.example.java21.workshop.support.WeatherRecord;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Travaux pratiques : application complète bâtie sur {@link CompletableFuture}.
 */
public final class CityAnalyticsApp {

    private final ExecutorService executor;
    private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    public CityAnalyticsApp(ExecutorService executor) {
        this.executor = executor;
    }

    public static void main(String[] args) throws Exception {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            new CityAnalyticsApp(executor).run();
        }
    }

    private void run() throws Exception {
        var start = Instant.now();
        var cities = List.of("Paris", "Lyon", "Marseille", "Toulouse", "Bordeaux");

        var weatherFuture = loadWeatherData(cities)
                .orTimeout(1, TimeUnit.SECONDS)
                .exceptionally(ex -> {
                    log("Données météo indisponibles : " + ex.getMessage());
                    return List.of();
                });

        var trafficFuture = loadTrafficData(cities);

        var rankedCities = weatherFuture
                .thenCombineAsync(trafficFuture, this::mergeData, executor)
                .thenApplyAsync(this::rankCities, executor)
                .completeOnTimeout(List.of(), 2, TimeUnit.SECONDS)
                .join();

        rankedCities.forEach(report -> log("%s → indice %.2f, trafic %d min"
                .formatted(report.city(), report.comfortIndex(), report.trafficDelayMinutes())));

        monitorProcess();
        launchExporter();

        log("Temps total %d ms (threads actifs=%d)"
                .formatted(Duration.between(start, Instant.now()).toMillis(), threadMXBean.getThreadCount()));
    }

    private CompletableFuture<List<WeatherRecord>> loadWeatherData(List<String> cities) {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay(250);
            var random = ThreadLocalRandom.current();
            return cities.stream()
                    .map(city -> new WeatherRecord(city, 15 + random.nextDouble(12), 40 + random.nextDouble(50), Instant.now()))
                    .toList();
        }, executor);
    }

    private CompletableFuture<Map<String, Integer>> loadTrafficData(List<String> cities) {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay(300);
            var random = ThreadLocalRandom.current();
            return cities.stream().collect(Collectors.toMap(city -> city, city -> 5 + random.nextInt(25)));
        }, executor);
    }

    private List<CityReport> mergeData(List<WeatherRecord> weather, Map<String, Integer> traffic) {
        return weather.stream()
                .map(record -> new CityReport(
                        record.city(),
                        computeComfortIndex(record, traffic.getOrDefault(record.city(), 0)),
                        record,
                        traffic.getOrDefault(record.city(), 0)))
                .toList();
    }

    private List<CityReport> rankCities(List<CityReport> reports) {
        return reports.stream()
                .sorted((a, b) -> Double.compare(b.comfortIndex(), a.comfortIndex()))
                .toList();
    }

    private double computeComfortIndex(WeatherRecord record, int trafficDelay) {
        double weatherScore = 100 - Math.abs(22 - record.temperatureCelsius()) * 3;
        double humidityScore = 100 - Math.abs(50 - record.humidityPercentage()) * 1.2;
        double trafficPenalty = trafficDelay * 1.5;
        return Math.max(0, (weatherScore + humidityScore) / 2 - trafficPenalty);
    }

    private void monitorProcess() {
        var handle = ProcessHandle.current();
        var info = handle.info();
        log("Processus PID=%d, commande=%s"
                .formatted(handle.pid(), info.command().orElse("<java>")));
    }

    private void launchExporter() throws Exception {
        var process = new ProcessBuilder("bash", "-lc", "echo Export CSV && sleep 0.5").start();
        var handle = process.toHandle();
        handle.onExit().thenAccept(ph -> {
            var cpu = ph.info().totalCpuDuration().map(Duration::toMillis).map(ms -> ms + " ms").orElse("n/d");
            log("Export terminé (PID=%d, exit=%d, CPU=%s)".formatted(ph.pid(), process.exitValue(), cpu));
        }).join();
    }

    private void simulateDelay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void log(String message) {
        System.out.printf("[%s][%s] %s%n", Instant.now(), Thread.currentThread().getName(), message);
    }
}
