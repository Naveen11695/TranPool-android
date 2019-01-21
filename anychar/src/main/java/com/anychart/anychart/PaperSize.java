package com.anychart.anychart;

import java.util.Locale;

/**
 * Paper sizes.
 */
public enum PaperSize implements JsObject.JsObjectInterface {
    A0("a0"),
    A1("a1"),
    A2("a2"),
    A3("a3"),
    A4("a4"),
    A5("a5"),
    A6("a6"),
    US_LETTER("us-letter");

    private final String value;

    PaperSize(String value) {
        this.value = value;
    }

    public String generateJs() {
        return String.format(Locale.US, "\"%s\"", value);
    }
}