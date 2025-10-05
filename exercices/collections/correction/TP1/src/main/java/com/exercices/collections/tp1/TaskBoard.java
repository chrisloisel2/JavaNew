package com.exercices.collections.tp1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class TaskBoard {

    private final List<Task> tasks = new ArrayList<>();
    private final Map<String, Task> index = new java.util.HashMap<>();

    public void ajouter(Task task) {
        Objects.requireNonNull(task);
        if (index.containsKey(task.id())) {
            throw new IllegalArgumentException("Une tâche avec cet identifiant existe déjà");
        }
        tasks.add(task);
        index.put(task.id(), task);
    }

    public Optional<Task> supprimerParId(String id) {
        Task removed = index.remove(id);
        if (removed != null) {
            tasks.removeIf(task -> task.id().equals(id));
            return Optional.of(removed);
        }
        return Optional.empty();
    }

    public List<Task> rechercherParLabel(String label) {
        return tasks.stream()
                .filter(task -> task.labels().contains(label))
                .collect(Collectors.toList());
    }

    public Optional<Task> rechercherParId(String id) {
        return Optional.ofNullable(index.get(id));
    }

    public Map<Status, List<Task>> afficherParEtat() {
        Map<Status, List<Task>> resultat = new EnumMap<>(Status.class);
        for (Status status : Status.values()) {
            resultat.put(status, new ArrayList<>());
        }
        for (Task task : tasks) {
            resultat.get(task.status()).add(task);
        }
        Comparator<Task> comparator = Comparator.comparing(task -> task.titre().toLowerCase());
        resultat.values().forEach(list -> list.sort(comparator));
        return Collections.unmodifiableMap(resultat);
    }

    public List<Task> listerToutes() {
        return Collections.unmodifiableList(tasks);
    }
}
