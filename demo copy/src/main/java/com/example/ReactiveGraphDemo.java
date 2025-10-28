import java.util.concurrent.Flow.*;
import java.util.concurrent.*;
import java.util.*;
import java.util.function.*;

abstract class BaseProcessor extends SubmissionPublisher<String> implements Processor<String, String> {
    private final String name;
    private final List<Subscription> upstreams = Collections.synchronizedList(new ArrayList<>());

    protected BaseProcessor(String name) {
        super();
        this.name = name;
    }
    public String getName() { return name; }

    @Override
    public void onSubscribe(Subscription subscription) {
        upstreams.add(subscription);
        subscription.request(Long.MAX_VALUE);
    }
    @Override
    public void onError(Throwable throwable) {
        System.err.printf("[%s] onError: %s%n", name, throwable);
        closeExceptionally(throwable);
    }
    @Override
    public void onComplete() {
        System.out.printf("[%s] onComplete%n", name);
        close();
    }
    @Override
    public abstract void onNext(String item);

    public void cancelAllUpstreams() {
        synchronized (upstreams) {
            for (Subscription s : upstreams) {
                try { s.cancel(); } catch (Throwable ignored) {}
            }
            upstreams.clear();
        }
    }
}

class MapProcessor extends BaseProcessor {
    private final UnaryOperator<String> mapper;

    public MapProcessor(String name, UnaryOperator<String> mapper) {
        super(name);
		this.mapper = mapper;
    }

    @Override
    public void onNext(String item) {
        submit(mapper.apply(item));
    }
}

class FilterProcessor extends BaseProcessor {
    private final Predicate<String> predicate;

    public FilterProcessor(String name, Predicate<String> predicate) {
        super(name); this.predicate = predicate;
    }

    @Override
    public void onNext(String item) {
        if (predicate.test(item))submit(item);
    }
}

class BatchProcessor extends BaseProcessor {
    private final int size;
    private final List<String> buffer = Collections.synchronizedList(new ArrayList<>());

    public BatchProcessor(String name, int size) {
        super(name); this.size = Math.max(1, size);
    }

    @Override
    public void onNext(String item) {
        List<String> snapshot = null;
        synchronized (buffer) {
            buffer.add(item);
            if (buffer.size() >= size) {
                snapshot = new ArrayList<>(buffer);
                buffer.clear();
            }
        }
        if (snapshot != null) submit(String.join(" | ", snapshot));
    }
    @Override
    public void onComplete() {
        List<String> leftover = null;
        synchronized (buffer) {
            if (!buffer.isEmpty()) {
                leftover = new ArrayList<>(buffer);
                buffer.clear();
            }
        }
        if (leftover != null) submit(String.join(" | ", leftover));
        super.onComplete();
    }
}

class LoggingProcessor extends BaseProcessor {
    public LoggingProcessor(String name) { super(name); }
    @Override
    public void onNext(String item) {
        System.out.printf("[LOG %s] %s%n", getName(), item);
        submit(item);
    }
}

class EventPublisher extends SubmissionPublisher<String> {
    private final String name;
    public EventPublisher(String name) { super(); this.name = name; }
    public String getName() { return name; }
    public void emit(String item) { submit(item); }
}

class TerminalSubscriber implements Subscriber<String> {
    private final String name;
    private Subscription sub;
    public TerminalSubscriber(String name) { this.name = name; }

	@Override
    public void onSubscribe(Subscription subscription) {
        this.sub = subscription; subscription.request(Long.MAX_VALUE);
    }

	@Override
    public void onNext(String item) { System.out.printf("[SINK %s] <- %s%n", name, item); }

	@Override
    public void onError(Throwable throwable) { System.err.printf("[SINK %s] onError: %s%n", name, throwable); }

	@Override
    public void onComplete() { System.out.printf("[SINK %s] onComplete%n", name); }

	public void cancel() { if (sub != null) sub.cancel(); }
}

class ProcessorRegistry {
    private static final ProcessorRegistry INSTANCE = new ProcessorRegistry();

    private final Map<String, Object> components = new ConcurrentHashMap<>();


    private ProcessorRegistry() {}

    public static ProcessorRegistry get() { return INSTANCE; }


    public void register(String key, Object component) {
        components.put(Objects.requireNonNull(key), Objects.requireNonNull(component));
    }

    public Object get(String key) { return components.get(key); }



    public Map<String, Object> all() { return Collections.unmodifiableMap(components); }
}

class StreamOrchestrator {
    private final Map<String, Object> registry;
    public StreamOrchestrator(Map<String, Object> registry) { this.registry = registry; }

    public void reset() {
        for (Object c : registry.values()) {
            if (c instanceof BaseProcessor bp) bp.cancelAllUpstreams();
            else if (c instanceof TerminalSubscriber ts) ts.cancel();
        }
    }

    public void apply(List<Edge> edges) {
        reset();
        for (Edge e : edges) {
            Object upstream = registry.get(e.from());
            Object downstream = registry.get(e.to());
            if (!(upstream instanceof Publisher<?> pub)) {
                throw new IllegalArgumentException("Upstream n'est pas Publisher: " + e.from());
            }
            if (downstream instanceof Processor<?,?> proc) {
                @SuppressWarnings("unchecked") Processor<String,String> p = (Processor<String,String>) proc;
                @SuppressWarnings("unchecked") Publisher<String> up = (Publisher<String>) pub;
                up.subscribe(p);
            } else if (downstream instanceof Subscriber<?> sub) {
                @SuppressWarnings("unchecked") Subscriber<String> s = (Subscriber<String>) sub;
                @SuppressWarnings("unchecked") Publisher<String> up = (Publisher<String>) pub;
                up.subscribe(s);
            } else {
                throw new IllegalArgumentException("Downstream inconnu: " + e.to());
            }
        }
        System.out.println(">> Topologie appliquée (" + edges.size() + " liens).");
    }

    public static record Edge(String from, String to) { }
}

public class ReactiveGraphDemo {
    public static void main(String[] args) throws InterruptedException {
        EventPublisher source = new EventPublisher("SOURCE");
        MapProcessor map     = new MapProcessor("MAP", s -> "mapped(" + s.toUpperCase() + ")");
        FilterProcessor filter = new FilterProcessor("FILTER", s -> s.length() % 2 == 0);
        BatchProcessor batch  = new BatchProcessor("BATCH", 3);
        LoggingProcessor log  = new LoggingProcessor("LOG");
        TerminalSubscriber sink = new TerminalSubscriber("PRINT");

        ProcessorRegistry reg = ProcessorRegistry.get();
        reg.register("SOURCE", source);
        reg.register("MAP",    map);
        reg.register("FILTER", filter);
        reg.register("BATCH",  batch);
        reg.register("LOG",    log);
        reg.register("SINK",   sink);

        StreamOrchestrator orch = new StreamOrchestrator(reg.all());

        List<StreamOrchestrator.Edge> T1 = List.of(
            new StreamOrchestrator.Edge("SOURCE", "MAP"),
            new StreamOrchestrator.Edge("MAP",    "FILTER"),
            new StreamOrchestrator.Edge("FILTER", "SINK"),
            new StreamOrchestrator.Edge("SOURCE", "LOG"),
            new StreamOrchestrator.Edge("LOG",    "SINK")
        );

        List<StreamOrchestrator.Edge> T2 = List.of(
            new StreamOrchestrator.Edge("SOURCE", "MAP"),
            new StreamOrchestrator.Edge("MAP",    "BATCH"),
            new StreamOrchestrator.Edge("BATCH",  "SINK"),
            new StreamOrchestrator.Edge("SOURCE", "LOG")
        );

        orch.apply(T1);
        emitBurst(source, "a", "bb", "ccc", "dddd", "eeeee");
        Thread.sleep(400);

        orch.apply(T2);
        emitBurst(source, "alpha", "beta", "gamma", "delta", "epsilon", "z");
        Thread.sleep(400);

        source.close();
        map.close();
        filter.close();
        batch.close();
        log.close();

        System.out.println(">> Terminé.");
    }

    private static void emitBurst(EventPublisher source, String... items) {
        for (String s : items) source.emit(s);
    }
}
