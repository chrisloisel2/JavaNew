package com.cours.exemple.basics.scope;

/**
 * Montre la portée des variables locales, d'instance et statiques.
 */
public final class ScopeDashboard {

    private static int totalDashboardsCreated = 0; // portée statique

    private int activeWidgets = 3; // portée d'instance

    private ScopeDashboard() {
        totalDashboardsCreated++;
    }

    public static void example() {
        System.out.println("[Portée] Suivi de métriques");

        ScopeDashboard dashboard = new ScopeDashboard();
        dashboard.render();
        System.out.printf("Nombre total de tableaux de bord: %d\n", totalDashboardsCreated);
    }

    private void render() {
        for (int i = 1; i <= activeWidgets; i++) { // i : portée locale
            String widgetName = "Widget-" + i; // variable locale dans la boucle
            System.out.printf("Chargement de %s\n", widgetName);
        }
    }
}
