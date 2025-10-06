package com.example.patterns.behavioral.visitor;

public sealed interface Expression permits Literal, Addition {
    <R> R accept(ExpressionVisitor<R> visitor);
}
