package com.exercices.collections.tp1;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class TaskBoardDemo {

    private TaskBoardDemo() {
    }

    public static void main(String[] args) {
        TaskBoard board = new TaskBoard();
        board.ajouter(new Task("T1", "Configurer le repo", Status.TODO, Set.of("setup", "git")));
        board.ajouter(new Task("T2", "Préparer la démo", Status.IN_PROGRESS, Set.of("demo")));
        board.ajouter(new Task("T3", "Livrer la fonctionnalité", Status.DONE, Set.of("delivery", "demo")));

        System.out.println("Rechercher par label 'demo' :");
        List<Task> demoTasks = board.rechercherParLabel("demo");
        demoTasks.forEach(task -> System.out.println(" - " + task));

        Map<Status, List<Task>> parEtat = board.afficherParEtat();
        parEtat.forEach((status, tasks) -> {
            System.out.println(status + " :");
            tasks.forEach(task -> System.out.println("  * " + task.titre()));
        });

        board.supprimerParId("T2");
        System.out.println("Indexation directe : " + board.rechercherParId("T1"));
    }
}
