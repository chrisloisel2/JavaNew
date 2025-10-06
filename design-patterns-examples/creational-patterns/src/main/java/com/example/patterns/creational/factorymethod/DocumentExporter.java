package com.example.patterns.creational.factorymethod;

/**
 * Exemple de Factory Method : chaque sous-classe choisit le format généré.
 */
public abstract class DocumentExporter {

    public final byte[] export(String title, String body) {
        return buildDocument(title, body);
    }

    protected abstract byte[] buildDocument(String title, String body);

    public static DocumentExporter pdf() {
        return new PdfExporter();
    }

    public static DocumentExporter markdown() {
        return new MarkdownExporter();
    }

    private static final class PdfExporter extends DocumentExporter {
        @Override
        protected byte[] buildDocument(String title, String body) {
            return ("PDF::" + title + "::" + body).getBytes();
        }
    }

    private static final class MarkdownExporter extends DocumentExporter {
        @Override
        protected byte[] buildDocument(String title, String body) {
            return ("# " + title + "\n\n" + body).getBytes();
        }
    }
}
