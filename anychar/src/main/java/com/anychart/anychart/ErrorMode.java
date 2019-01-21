package com.anychart.anychart;

import java.util.Locale;

/**
 * Series <a href="anychart.core.utils.Error#mode">error mode</a> enumeration.
 */
public enum ErrorMode implements JsObject.JsObjectInterface {
    BOTH("both"),
    NONE("none"),
    VALUE("value"),
    X("x");

    private final String value;

    ErrorMode(String value) {
        this.value = value;
    }

    public String generateJs() {
        return String.format(Locale.US, "\"%s\"", value);
    }
}