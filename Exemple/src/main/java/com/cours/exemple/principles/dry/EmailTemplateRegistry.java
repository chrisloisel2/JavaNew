package com.cours.exemple.principles.dry;

import java.util.Map;

/**
 * Centralise des modèles d'e-mails pour éviter la duplication de chaînes.
 */
public final class EmailTemplateRegistry {
    private static final Map<String, String> TEMPLATES = Map.of(
            "WELCOME", "Bonjour %s, merci de rejoindre notre communauté !",
            "REMINDER", "Bonjour %s, pensez à finaliser votre profil.",
            "THANK_YOU", "Merci %s pour votre confiance."
    );

    private EmailTemplateRegistry() {
    }

    public static void example() {
        System.out.println("[DRY] Réutilisation de modèles d'e-mails");
        send("WELCOME", "Aïcha");
        send("REMINDER", "Omar");
        send("THANK_YOU", "Chloé");
    }

    private static void send(String templateKey, String recipient) {
        String template = TEMPLATES.get(templateKey);
        if (template == null) {
            throw new IllegalArgumentException("Template inconnu: " + templateKey);
        }
        System.out.println(template.formatted(recipient));
    }
}
