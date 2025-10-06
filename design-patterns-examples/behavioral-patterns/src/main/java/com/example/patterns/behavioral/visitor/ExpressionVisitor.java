package com.example.patterns.behavioral.visitor;

public interface ExpressionVisitor<R> {
    R visitLiteral(Literal literal);
    R visitAddition(Addition addition);
}
