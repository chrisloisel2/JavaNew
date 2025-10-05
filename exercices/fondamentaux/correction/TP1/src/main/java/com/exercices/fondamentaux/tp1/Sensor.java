package com.exercices.fondamentaux.tp1;

import java.time.Instant;
import java.util.Objects;

/**
 * Représente un capteur avec les différentes catégories de types primitifs demandées.
 */
record Sensor(int id, String emplacement, double derniereMesure, float seuilCritique, boolean actif,
              long serie, Instant derniereLecture) {

    Sensor {
        Objects.requireNonNull(emplacement, "emplacement");
        Objects.requireNonNull(derniereLecture, "derniereLecture");
    }

    String toSummary() {
        return String.format("Capteur #%d (%s)%n- Mesure actuelle : %.2f°C%n- Seuil critique : %.2f°C%n- Actif : %s%n- Série : %d%n- Dernière lecture : %s", id,
                emplacement, derniereMesure, seuilCritique, actif ? "oui" : "non", serie, derniereLecture);
    }
}
