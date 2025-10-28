import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.Flow.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.stream.Stream;


public class Main {

    record Result(String sourceId, String line, long wordCount) { }

    public static void main(String[] args) throws Exception {
        ExecutorService vexec = Executors.newVirtualThreadPerTaskExecutor();

        try (SubmissionPublisher<String> upstream = new SubmissionPublisher<>(vexec, Flow.defaultBufferSize())) {

            WordCountProcessor processor = new WordCountProcessor(vexec, "SRC-1");

            PrintingSubscriber sink = new PrintingSubscriber(5);

            upstream.subscribe(processor);
            processor.subscribe(sink);

            try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                scope.fork(() -> produce(upstream, List.of(
                        "Hello from virtual threads!",
                        "Reactive Streams with Flow are neat",
                        "Java 21 brings Loom and structured concurrency",
                        "mapMulti lets us flatten elegantly",
                        "Backpressure keeps things stable"
                )));

                scope.fork(() -> produce(upstream, List.of(
                        "Pattern matching switch is delightful",
                        "Records make DTOs trivial",
                        "Project Loom makes blocking I/O cheap",
                        "This line \t has\tmixed  whitespace",
                        "Done"
                )));

                scope.join();
                scope.throwIfFailed();
            }

            upstream.close();

            Thread.sleep(300);
        } finally {
            vexec.shutdown();
        }
    }

    static void produce(SubmissionPublisher<String> pub, List<String> lines) {
        Stream<String> stream = lines.stream()
                .takeWhile(s -> !Objects.equals(s, "Done")) // Java 9
                .map(String::strip)                          // Java 11
                .mapMulti((String s, java.util.function.Consumer<String> out) -> {
                    if (s.contains("\t")) {
                        for (String part : s.split("\t")) out.accept(part.strip());
                    } else {
                        out.accept(s);
                    }
                });

        stream.forEach(s -> {
            String routed = switch (s) {
                case null, "" -> "<empty>";
                default -> s;
            };

            pub.submit(routed);
        });
    }

    static final class WordCountProcessor extends SubmissionPublisher<Result> implements Processor<String, Result> {
        private Subscription subscription;

        @Override public void onSubscribe(Subscription s) {
            this.subscription = s;
            requestMore(1);
        }

        @Override public void onNext(String item) {
			// Traitement
            submit();

            requestMore(1);
        }

        @Override public void onError(Throwable t) {
            this.closeExceptionally(t);
        }

        @Override public void onComplete() {
            this.close();
        }

        private void requestMore(long n) {
            if (subscription != null) subscription.request(n);
        }
    }

    static final class PrintingSubscriber implements Subscriber<Result> {
        private final int batchSize;
        private Subscription subscription;
        private int receivedInBatch;

        PrintingSubscriber(int batchSize) { this.batchSize = batchSize; }

        @Override public void onSubscribe(Subscription s) {
            this.subscription = s;
            this.receivedInBatch = 0;
            s.request(batchSize);
        }

        @Override public void onNext(Result item) {
            // Pretend to do blocking I/O per item (cheap with virtual threads)
            try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
                exec.submit(() -> {
                    System.out.printf("[%s] %s -> %d words%n",
                            Thread.currentThread().getName(), item.line(), item.wordCount());
                    try { Thread.sleep(25); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
                }).get();
            } catch (Exception e) {
                onError(e);
                return;
            }

            if (++receivedInBatch >= batchSize) {
                receivedInBatch = 0;
                subscription.request(batchSize);
            }
        }

        @Override public void onError(Throwable t) {
            System.err.println("Sink failed: " + t);
        }

        @Override public void onComplete() {
            System.out.println("Sink complete.");
        }
    }
}
