package com.example.backend.exception;

/**
 * Sealed interface pour repr\u00e9senter les r\u00e9sultats d'API
 * Utilise les Sealed Classes de Java pour un contr\u00f4le strict des types
 */
public sealed interface ApiResult<T> permits ApiResult.Success, ApiResult.Failure {

    /**
     * R\u00e9sultat de succ\u00e8s contenant les donn\u00e9es
     */
    record Success<T>(T data) implements ApiResult<T> {}

    /**
     * R\u00e9sultat d'\u00e9chec contenant l'erreur
     */
    record Failure<T>(ApiError error) implements ApiResult<T> {}

    /**
     * Cr\u00e9e un r\u00e9sultat de succ\u00e8s
     */
    static <T> ApiResult<T> success(T data) {
        return new Success<>(data);
    }

    /**
     * Cr\u00e9e un r\u00e9sultat d'\u00e9chec
     */
    static <T> ApiResult<T> failure(ApiError error) {
        return new Failure<>(error);
    }

    /**
     * V\u00e9rifie si le r\u00e9sultat est un succ\u00e8s
     */
    default boolean isSuccess() {
        return this instanceof Success;
    }

    /**
     * V\u00e9rifie si le r\u00e9sultat est un \u00e9chec
     */
    default boolean isFailure() {
        return this instanceof Failure;
    }

    /**
     * R\u00e9cup\u00e8re les donn\u00e9es ou lance une exception
     */
    default T getOrThrow() {
        return switch (this) {
            case Success<T>(var data) -> data;
            case Failure<T>(var error) -> throw new RuntimeException(error.message());
        };
    }
}
