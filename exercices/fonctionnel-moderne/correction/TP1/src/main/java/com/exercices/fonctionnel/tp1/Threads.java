import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.stream.*;

public class ThreadFeaturesDemo {

    static final ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();

    public static void main(String[] args) throws Exception {

        runClassicThreads();
        runVirtualThreads();
        runStructuredConcurrency();
        runScopedValuesDemo();
        runCompletableFutureDemo();
        runLocksDemo();
    }

    static void runClassicThreads() throws InterruptedException {
        System.out.println("➡️ Threads classiques :");
        ExecutorService pool = Executors.newFixedThreadPool(4);

        long start = System.currentTimeMillis();

        List<Callable<String>> tasks = IntStream.range(0, 10)
                .mapToObj(i -> (Callable<String>) () -> {
                    Thread.sleep(200);
                    return "Task " + i + " via " + Thread.currentThread();
                }).toList();

        var results = pool.invokeAll(tasks);
        for (var r : results) System.out.println(r);
        pool.shutdown();

        System.out.println("⏱ Durée : " + (System.currentTimeMillis() - start) + "ms\n");
    }

    static void runVirtualThreads() throws InterruptedException, ExecutionException {
        System.out.println("➡️ Threads virtuels (Java 21) :");

        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
            long start = System.currentTimeMillis();

            var futures = IntStream.range(0, 10)
                    .mapToObj(i -> exec.submit(() -> {
                        Thread.sleep(200);
                        return "Task " + i + " via " + Thread.currentThread();
                    }))
                    .toList();

            for (var f : futures) System.out.println(f.get());

            System.out.println("⏱ Durée : " + (System.currentTimeMillis() - start) + "ms\n");
        }
    }

    // 🔸 Structured Concurrency (Java 21)
    static void runStructuredConcurrency() throws Exception {
        System.out.println("➡️ Structured Concurrency :");

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var userTask = scope.fork(() -> fetchUser());
            var ordersTask = scope.fork(() -> fetchOrders());
            var notificationsTask = scope.fork(() -> fetchNotifications());

            scope.join(); // attend toutes les tâches
            scope.throwIfFailed(); // si une tâche a échoué, on stoppe tout

            System.out.println("Résultats : " +
                    userTask.resultNow() + ", " +
                    ordersTask.resultNow() + ", " +
                    notificationsTask.resultNow());
        }

        System.out.println();
    }

    static String fetchUser() throws InterruptedException {
        Thread.sleep(200);
        return "👤 Utilisateur";
    }

    static String fetchOrders() throws InterruptedException {
        Thread.sleep(300);
        return "🛒 Commandes";
    }

    static String fetchNotifications() throws InterruptedException {
        Thread.sleep(250);
        return "🔔 Notifications";
    }

    // 🔸 Scoped Values (Java 21)
    static void runScopedValuesDemo() throws Exception {
        System.out.println("➡️ Scoped Values (vs ThreadLocal) :");

        ScopedValue.where(REQUEST_ID, UUID.randomUUID().toString()).run(() -> {
            try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
                exec.submit(() -> logWithRequest("Lecture base de données")).get();
                exec.submit(() -> logWithRequest("Traitement en mémoire")).get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println();
    }

    static void logWithRequest(String msg) {
        System.out.printf("[%s] %s - Thread: %s%n",
                REQUEST_ID.get(), msg, Thread.currentThread().getName());
    }

    // 🔸 CompletableFuture et parallélisme moderne
    static void runCompletableFutureDemo() throws Exception {
        System.out.println("➡️ CompletableFuture et flux parallèles :");

        var start = Instant.now();

        var cf = CompletableFuture.supplyAsync(() -> slowTask("Analyse"))
                .thenCombine(CompletableFuture.supplyAsync(() -> slowTask("Export")),
                        (a, b) -> a + " & " + b)
                .thenApply(res -> "Résultat combiné → " + res);

        System.out.println(cf.get());
        System.out.println("⏱ " + Duration.between(start, Instant.now()).toMillis() + "ms\n");
    }

    static String slowTask(String name) {
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        return name + " OK";
    }

    // 🔸 Synchronisation moderne (Locks, try-with-resources)
    static void runLocksDemo() {
        System.out.println("➡️ Synchronisation avec ReentrantLock :");

        var lock = new ReentrantLock();
        var sharedList = new ArrayList<String>();

        Runnable task = () -> {
            lock.lock();
            try {
                sharedList.add(Thread.currentThread().getName());
            } finally {
                lock.unlock();
            }
        };

        try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10).forEach(i -> exec.submit(task));
        }

        System.out.println("Liste finale : " + sharedList + "\n");
    }
}
