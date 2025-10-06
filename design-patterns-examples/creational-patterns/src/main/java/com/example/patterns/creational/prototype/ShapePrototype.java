package com.example.patterns.creational.prototype;

/**
 * Prototype basé sur {@link Cloneable} pour dupliquer rapidement un objet.
 */
public class ShapePrototype implements Cloneable {

    private String type;
    private int size;

    public ShapePrototype(String type, int size) {
        this.type = type;
        this.size = size;
    }

    public String type() {
        return type;
    }

    public int size() {
        return size;
    }

    public void resize(int size) {
        this.size = size;
    }

    @Override
    public ShapePrototype clone() {
        try {
            return (ShapePrototype) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}
