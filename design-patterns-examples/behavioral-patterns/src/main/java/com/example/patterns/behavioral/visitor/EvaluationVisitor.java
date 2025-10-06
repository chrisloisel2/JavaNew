package com.example.patterns.behavioral.visitor;

public class EvaluationVisitor implements ExpressionVisitor<Integer> {
    @Override
    public Integer visitLiteral(Literal literal) {
        return literal.value();
    }

    @Override
    public Integer visitAddition(Addition addition) {
        return addition.left().accept(this) + addition.right().accept(this);
    }
}
