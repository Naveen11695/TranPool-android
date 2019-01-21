package com.anychart.anychart;

import java.util.Locale;

/**
 * List of data source for a price indicator value.
 */
public enum PriceIndicatorDataSource implements JsObject.JsObjectInterface {
    FIRST_VISIBLE("first-visible"),
    LAST_VISIBLE("last-visible"),
    SERIES_END("series-end"),
    SERIES_START("series-start");

    private final String value;

    PriceIndicatorDataSource(String value) {
        this.value = value;
    }

    public String generateJs() {
        return String.format(Locale.US, "\"%s\"", value);
    }
}