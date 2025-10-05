# TP2 – Architecture SOLID pour une boutique en ligne

## Objectifs pédagogiques
- Appliquer les principes SOLID.
- Mettre en œuvre des interfaces et l'inversion de dépendances.
- Utiliser les classes scellées et records pour modéliser des données modernes.

## Mise en situation
Vous conseillez une boutique en ligne souhaitant refondre son module de commande. Le code historique est monolithique et difficile à tester.

## Exercices
1. **Analyse du legacy**  
   Partez d'une classe fictive `OrderProcessorLegacy` qui gère création, validation, paiement et expédition. Rédigez un court document (commentaires) listant les problèmes vis-à-vis de SOLID.

2. **Refactorisation SRP/OCP**  
   Créez des interfaces séparées (`OrderCreator`, `OrderValidator`, `OrderDispatcher`). Implémentez des classes concrètes respectant SRP. Montrez comment substituer une implémentation sans modifier le code client (OCP).

3. **LSP et DIP**  
   Introduisez une hiérarchie `sealed interface PaymentMethod permits CardPayment, WalletPayment`. Injectez la méthode de paiement via le constructeur d'un `CheckoutService`. Vérifiez que chaque implémentation respecte le contrat (LSP).

4. **Test d'intégration léger**  
   Écrivez un scénario `main` qui compose les services et traite plusieurs commandes. Utilisez des mocks simples (classes internes) pour simuler l'expédition et démontrer l'inversion de dépendances.

## Pistes d'extension
- Brancher un journal (`Logger`) injecté pour suivre les étapes.
- Ajouter une implémentation de paiement différé avec validation supplémentaire.
