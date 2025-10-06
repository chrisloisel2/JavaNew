package com.example.java21.project;

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
 * Atelier complet couvrant la gestion des {@link java.util.concurrent.CompletableFuture} et de
 * {@link ProcessHandle} afin de démontrer les nouveautés introduites depuis Java&nbsp;9.
 */
public final class CompletableFutureWorkshop {

    private final ExecutorService executor;
    private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    private CompletableFutureWorkshop(ExecutorService executor) {
        this.executor = executor;
    }

    public static void main(String[] args) throws Exception {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            new CompletableFutureWorkshop(executor).run();
        }
    }

    private void run() throws Exception {
        log("Démarrage du pipeline d'analyse urbaine sur thread virtuel=%s".formatted(Thread.currentThread().isVirtual()));
        var globalStart = Instant.now();
        var cities = List.of("Paris", "Lyon", "Marseille", "Toulouse", "Bordeaux");

        var weatherFuture = loadWeatherData(cities)
                .orTimeout(1, TimeUnit.SECONDS) // Java 9 : détection d'un blocage
                .exceptionally(ex -> {
                    log("Les données météo ne sont pas disponibles : %s".formatted(ex.getMessage()));
                    return List.of();
                });

        var trafficFuture = loadTrafficData(cities);

        var combinedFuture = weatherFuture.thenCombineAsync(trafficFuture, this::mergeData, executor)
                .thenApplyAsync(this::rankCities, executor)
                .completeOnTimeout(List.of(), 2, TimeUnit.SECONDS);

        var reports = combinedFuture.join();
        reports.forEach(report ->
                log("Ville %s → indice confort %.2f, retard trafic %d min"
                        .formatted(report.city(), report.comfortIndex(), report.trafficDelayMinutes())));

        monitorCurrentProcess();
        launchReportExporterProcess();

        var totalDuration = Duration.between(globalStart, Instant.now());
        log("Pipeline complet exécuté en %d ms (threads utilisés=%d)".formatted(
                totalDuration.toMillis(), threadMXBean.getThreadCount()));
    }

    private CompletableFuture<List<WeatherRecord>> loadWeatherData(List<String> cities) {
        return CompletableFuture.supplyAsync(() -> {
            var start = Instant.now();
            sleep(250);
            var random = ThreadLocalRandom.current();
            var result = cities.stream()
                    .map(city -> new WeatherRecord(
                            city,
                            15 + random.nextDouble(12),
                            40 + random.nextDouble(50),
                            Instant.now()))
                    .toList();
            log("Données météo récupérées en %d ms".formatted(Duration.between(start, Instant.now()).toMillis()));
            return result;
        }, executor);
    }

    private CompletableFuture<Map<String, Integer>> loadTrafficData(List<String> cities) {
        return CompletableFuture.supplyAsync(() -> {
            var start = Instant.now();
            sleep(300);
            var random = ThreadLocalRandom.current();
            var result = cities.stream().collect(Collectors.toMap(city -> city, city -> 5 + random.nextInt(25)));
            log("Données trafic récupérées en %d ms".formatted(Duration.between(start, Instant.now()).toMillis()));
            return result;
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

    private void monitorCurrentProcess() {
        var handle = ProcessHandle.current();
        var info = handle.info();
        var command = info.command().orElse("<java>");
        var args = info.arguments()
                .map(array -> String.join(" ", array))
                .orElse("<aucun argument>");
        log("Processus courant PID=%d, commande=%s, arguments=%s"
                .formatted(handle.pid(), command, args));
    }

    private void launchReportExporterProcess() throws Exception {
        var process = new ProcessBuilder("bash", "-lc", "echo Export vers CSV && sleep 1")
                .start();
        var handle = process.toHandle();
        log("Sous-processus démarré PID=%d".formatted(handle.pid()));
        handle.onExit().thenAccept(ph -> {
            var exitCode = process.exitValue();
            var cpu = ph.info().totalCpuDuration().map(Duration::toMillis).map(millis -> millis + " ms").orElse("n/d");
            log("Sous-processus %d terminé (exit=%d, CPU=%s)".formatted(ph.pid(), exitCode, cpu));
        }).join();
    }

    private void sleep(long millis) {
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
