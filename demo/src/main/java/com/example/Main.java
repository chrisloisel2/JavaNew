package com.example;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.io.IOException;
import java.io.File;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder("ping", "-c", "2", "google.com");
        builder.inheritIO();
        Process process = builder.start();

        ProcessHandle handle = process.toHandle();
        System.out.println("PID : " + handle.pid());
        ProcessHandle.Info info = handle.info();
        info.command().ifPresent(cmd -> System.out.println("Commande : " + cmd));
        info.startInstant().ifPresent(time -> System.out.println("Démarré à : " + time));
        info.totalCpuDuration().ifPresent(cpu -> System.out.println("CPU : " + cpu));

        CompletableFuture<Process> future = process.onExit();
        future.thenAccept(p -> System.out.println("Terminé avec le code : " + p.exitValue()));
        process.waitFor();
        ProcessHandle current = ProcessHandle.current();
        System.out.println("PID du processus Java : " + current.pid());
        current.children().forEach(child -> System.out.println("Sous-processus : " + child.pid()));


        ProcessBuilder redir = new ProcessBuilder("ls", "-la");
        redir.redirectOutput(new File("output.txt"));
        redir.redirectError(new File("error.txt"));
        Process p2 = redir.start();
        p2.waitFor();
    }
}

