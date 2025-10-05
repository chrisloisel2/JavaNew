package com.exercices.exceptionsio.tp1;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class DirectoryWatcher implements AutoCloseable {

    private final WatchService watchService;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final FileCopier copier;
    private final Path destinationDirectory;

    public DirectoryWatcher(Path directory, FileCopier copier, Path destinationDirectory) throws IOException {
        this.watchService = FileSystems.getDefault().newWatchService();
        directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        this.copier = copier;
        this.destinationDirectory = destinationDirectory;
    }

    public void start(Path sourceDirectory) {
        executor.submit(() -> {
            try {
                WatchKey key;
                while ((key = watchService.take()) != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        Path created = sourceDirectory.resolve((Path) event.context());
                        Path destination = destinationDirectory.resolve(created.getFileName());
                        copier.copier(created, destination);
                    }
                    key.reset();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    @Override
    public void close() throws IOException {
        executor.shutdownNow();
        watchService.close();
    }
}
