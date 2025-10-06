package com.example.patterns.behavioral.visitor;

public record Literal(int value) implements Expression {
    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitLiteral(this);
    }
}
