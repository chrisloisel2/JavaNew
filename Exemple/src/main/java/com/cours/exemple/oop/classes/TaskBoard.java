package com.cours.exemple.oop.classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire de tâches démontrant la création de classes, attributs et méthodes.
 */
public final class TaskBoard {
    private final List<Task> tasks = new ArrayList<>();

    private TaskBoard() {
    }

    public static void example() {
        System.out.println("[Classes] Gestionnaire de tâches d'équipe");
        TaskBoard board = new TaskBoard();
        board.addTask(new Task("Préparer la démo", LocalDate.now().plusDays(2)));
        board.addTask(new Task("Mettre à jour la documentation", LocalDate.now().plusDays(5)));
        board.addTask(Task.ofUrgent("Corriger le bug de production"));
        board.display();
    }

    private void addTask(Task task) {
        tasks.add(task);
    }

    private void display() {
        tasks.forEach(task -> System.out.printf("- %s (échéance: %s)\n", task.title, task.dueDate));
    }

    /**
     * Classe interne représentant une tâche élémentaire.
     */
    private static final class Task {
        private final String title;
        private final LocalDate dueDate;

        Task(String title, LocalDate dueDate) {
            this.title = title;
            this.dueDate = dueDate;
        }

        static Task ofUrgent(String title) {
            return new Task(title, LocalDate.now().plusDays(1));
        }
    }
}
