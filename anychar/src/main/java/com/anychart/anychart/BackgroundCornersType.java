package com.anychart.anychart;

import java.util.Locale;

/**
 * Types of the <a href="anychart.core.ui.Background#cornerType">corner</a>.
 */
public enum BackgroundCornersType implements JsObject.JsObjectInterface {
    CUT("cut"),
    NONE("none"),
    ROUND("round"),
    ROUND_INNER("round-inner");

    private final String value;

    BackgroundCornersType(String value) {
        this.value = value;
    }

    public String generateJs() {
        return String.format(Locale.US, "\"%s\"", value);
    }
}