package com.anychart.anychart;

import java.util.Locale;

/**
 * Defines visibility in block, of text can't be shown in the area.
 */
public enum TextOverflow implements JsObject.JsObjectInterface {
    CLIP(""),
    ELLIPSIS("...");

    private final String value;

    TextOverflow(String value) {
        this.value = value;
    }

    public String generateJs() {
        return String.format(Locale.US, "\"%s\"", value);
    }
}