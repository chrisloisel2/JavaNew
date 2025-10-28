import java.util.concurrent.*;
import java.util.*;

public class CompletableFutureExample {

    private static String fetchData(String source) {
        try {
            System.out.println("Fetching data from " + source + " on thread " + Thread.currentThread().getName());
            Thread.sleep((long) (1000 + Math.random() * 2000));
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        return "Data from " + source;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        CompletableFuture<String> api1 = CompletableFuture.supplyAsync(() -> fetchData("API 1"));
        CompletableFuture<String> api2 = CompletableFuture.supplyAsync(() -> fetchData("API 2"));
        CompletableFuture<String> api3 = CompletableFuture.supplyAsync(() -> fetchData("API 3"));

        CompletableFuture<Void> allOf = CompletableFuture.allOf(api1, api2, api3);

        CompletableFuture<List<String>> combinedResults = allOf.thenApply(v -> {
            return List.of(api1.join(), api2.join(), api3.join());
        });

        CompletableFuture<Void> finalTask = combinedResults.thenAccept(results -> {
            System.out.println("\n--- Résultats combinés ---");
            results.forEach(System.out::println);
            System.out.println("\nTraitement terminé en " + (System.currentTimeMillis() - start) + " ms");
        });

        finalTask.join();
    }
}
