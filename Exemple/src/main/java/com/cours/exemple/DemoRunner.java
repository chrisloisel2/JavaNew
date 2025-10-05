package com.cours.exemple;

import com.cours.exemple.basics.comments.ConfigurationTemplate;
import com.cours.exemple.basics.controlflow.CreditApprovalSimulator;
import com.cours.exemple.basics.loops.InventoryBalancer;
import com.cours.exemple.basics.methods.FitnessCalculator;
import com.cours.exemple.basics.operators.DiscountEngine;
import com.cours.exemple.basics.scope.ScopeDashboard;
import com.cours.exemple.basics.strings.GreetingComposer;
import com.cours.exemple.basics.variables.CustomerProfile;
import com.cours.exemple.oop.classes.TaskBoard;
import com.cours.exemple.oop.encapsulation.BankAccount;
import com.cours.exemple.oop.inheritance.TransportationShowcase;
import com.cours.exemple.oop.interfaces.NotificationDemo;
import com.cours.exemple.oop.polymorphism.ReportPrinter;
import com.cours.exemple.principles.dry.EmailTemplateRegistry;
import com.cours.exemple.principles.kiss.BudgetCalculator;
import com.cours.exemple.principles.solid.OrderProcessingDemo;

/**
 * Point d'entrée unique permettant d'exécuter chaque scénario.
 */
public final class DemoRunner {

    private DemoRunner() {
        // Utilitaire : pas d'instanciation
    }

    public static void main(String[] args) {
        System.out.println("=== Exemples du cours Java 21 ===\n");
        runBasics();
        runOop();
        runPrinciples();
    }

    public static void runBasics() {
        System.out.println("-- Bases du langage --");
        CustomerProfile.example();
        CreditApprovalSimulator.example();
        InventoryBalancer.example();
        FitnessCalculator.example();
        GreetingComposer.example();
        DiscountEngine.example();
        ConfigurationTemplate.example();
        ScopeDashboard.example();
        System.out.println();
    }

    public static void runOop() {
        System.out.println("-- Programmation orientée objet --");
        TaskBoard.example();
        BankAccount.example();
        TransportationShowcase.example();
        NotificationDemo.example();
        ReportPrinter.example();
        System.out.println();
    }

    public static void runPrinciples() {
        System.out.println("-- Principes de conception --");
        EmailTemplateRegistry.example();
        BudgetCalculator.example();
        OrderProcessingDemo.example();
        System.out.println();
    }
}
