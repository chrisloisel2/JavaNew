package com.example.java21;

import static java.lang.StringTemplate.Processor.*;

/**
 * String Templates (preview) offrent une interpolation intégrée.
 */
public class StringTemplateDemo {

    public String greet(String name, int year) {
        return STR."Bienvenue \{name}, nous sommes en \{year}";
    }
}
