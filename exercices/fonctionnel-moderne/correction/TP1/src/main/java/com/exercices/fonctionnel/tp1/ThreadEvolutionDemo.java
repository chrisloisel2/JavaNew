import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.Flow.*;
import java.util.concurrent.locks.*;
import java.util.stream.*;

public class ThreadEvolutionDemo {

    public static void main(String[] args) throws Exception {
        section("Threads classiques"); classicThreads();
        section("Callable + Future"); callableFuture();
        section("CompletableFuture"); completableFuture();
        section("Parallel Streams"); parallelStreams();
        section("Virtual Threads (Java 21)"); virtualThreads();
        section("Structured Concurrency (Java 21)"); structuredConcurrency();
        section("Scoped Values (Java 21)"); scopedValues();
        System.out.println("\n✅ Fin de la démo");
    }

    static void section(String title){
        System.out.println("\n==============================");
        System.out.println(title);
        System.out.println("==============================");
    }

	public class MyThread extends Thread {

		private Function<String, String> f;

		//Constructeur
		public MyThread(Function<String, String> f, String name) {
			this.f = f;
			super(name);
		}

		@Override
		public void run() {
			f.apply("Bonjour depuis " + Thread.currentThread().getName());
			try { Thread.sleep(150); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
		}
	}



    static void classicThreads() throws InterruptedException {
        Thread t = new Thread(
		() -> {
            System.out.println("Bonjour depuis " + Thread.currentThread().getName());
            try { Thread.sleep(150); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        },
		 "mon-thread");
        t.start();
        t.join();
    }


    // Callable + Future (Java 5)
    static void callableFuture() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(2); // 2 threads dans le pool
        try {
            Future<Integer> f = pool.submit(() -> { Thread.sleep(100); return 42; });
            System.out.println("Résultat Future = " + f.get()); // get() bloque si nécessaire
        } finally {
            pool.shutdown();
        }
    }

    // CompletableFuture (Java 8)
    static void completableFuture() throws Exception {
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> slow("A"))
            .thenCombine(CompletableFuture.supplyAsync(() -> slow("B")), (a,b) -> a+b)
            .thenApply(res -> "Résultat combiné → " + res);
        System.out.println(cf.get());
    }

    static String slow(String name){
        try { Thread.sleep(150); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        return name;
    }

    // Parallel Streams (Java 8)
    static void parallelStreams(){
        var data = IntStream.rangeClosed(1, 8).boxed().toList();
        int sum = data.parallelStream().mapToInt(i -> i * i).sum();
        System.out.println("Somme des carrés = " + sum);
    }

    // Virtual Threads (Java 21)
    static void virtualThreads() throws Exception {
        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = IntStream.range(0, 5).mapToObj(i -> exec.submit(() -> {
                Thread.sleep(200); // blocage bon marché
                return "VT-" + i + " via " + Thread.currentThread();
            })).toList();
            for (var f : futures) System.out.println(f.get());
        }
    }

    // Structured Concurrency (Java 21)
    static void structuredConcurrency() throws Exception {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var a = scope.fork(() -> { Thread.sleep(150); return "A"; });
            var b = scope.fork(() -> { Thread.sleep(200); return "B"; });
            scope.join();
            scope.throwIfFailed();
            System.out.println("Résultats = " + a.resultNow() + ", " + b.resultNow());
        }
    }

    //Scoped Values (Java 21)
    static final ScopedValue<String> REQ_ID = ScopedValue.newInstance();

    static void scopedValues() throws Exception {
        ScopedValue.where(REQ_ID, UUID.randomUUID().toString()).run(() -> {
            try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
                exec.submit(() -> log("lecture DB")).get();
                exec.submit(() -> log("traitement" )).get();
            } catch (Exception e) { throw new RuntimeException(e); }
        });
    }

    static void log(String msg){
        System.out.printf("[%s] %s — %s%n", REQ_ID.get(), msg, Thread.currentThread().getName());
    }


}
