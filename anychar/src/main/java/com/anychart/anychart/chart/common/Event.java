package com.anychart.anychart.chart.common;

import java.util.Map;

public class Event {

    private Map<String, String> data;

    Event(Map<String, String> data) {
        this.data = data;
    }

    public Map<String, String> getData() {
        return data;
    }

}
