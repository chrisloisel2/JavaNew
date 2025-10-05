package com.cours.exemple.basics.comments;

/**
 * Exemple de classe richement commentée pour illustrer la syntaxe des commentaires.
 */
public final class ConfigurationTemplate {

    private ConfigurationTemplate() {
    }

    /**
     * Méthode principale illustrant l'usage de commentaires sur une ligne et multilignes.
     */
    public static void example() {
        System.out.println("[Commentaires] Lecture d'une configuration applicative");

        // Valeurs par défaut de la configuration
        String environment = "production";
        int timeoutSeconds = 30;
        boolean enableMetrics = true;

        /*
         * Vérifie que le délai d'attente est raisonnable.
         * Ce bloc multi-lignes documente une règle métier importante.
         */
        if (timeoutSeconds < 5) {
            System.out.println("Avertissement: délai trop court");
        }

        System.out.printf("Environnement: %s, Timeout: %ds, Metrics: %b\n",
                environment, timeoutSeconds, enableMetrics);
    }
}
