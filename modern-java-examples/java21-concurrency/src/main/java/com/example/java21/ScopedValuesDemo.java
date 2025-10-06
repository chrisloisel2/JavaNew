package com.example.java21;

import java.lang.ScopedValue;

/**
 * Scoped values (preview) offrent une alternative à ThreadLocal pour partager un contexte immuable.
 */
public class ScopedValuesDemo {

    private static final ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();

    public String runInScope(String requestId) {
        return ScopedValue.where(REQUEST_ID, requestId)
                .call(() -> "Traitement pour " + REQUEST_ID.get());
    }
}
