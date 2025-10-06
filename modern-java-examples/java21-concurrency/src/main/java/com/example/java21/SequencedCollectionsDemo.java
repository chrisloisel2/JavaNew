package com.example.java21;

import java.util.LinkedHashSet;
import java.util.SequencedSet;

/**
 * Sequenced collections conservent l'ordre d'insertion et fournissent getFirst/getLast.
 */
public class SequencedCollectionsDemo {

    public String firstAndLast() {
        SequencedSet<String> set = new LinkedHashSet<>();
        set.add("alpha");
        set.add("beta");
        set.add("gamma");
        return set.getFirst() + " - " + set.getLast();
    }
}
