package naveen.hackathon.hackathon.reports_charts;

import android.content.res.Resources;


import java.util.ArrayList;

import naveen.hackathon.hackathon.R;
import naveen.hackathon.hackathon.reports_charts.charts.AreaChartActivity;
import naveen.hackathon.hackathon.reports_charts.charts.LineChartActivity;
import naveen.hackathon.hackathon.reports_charts.charts.PieChartActivity;
import naveen.hackathon.hackathon.reports_charts.charts.linechart;

public class Chart {

    private String name;
    private Class activityClass;

    private Chart(String name, Class activityClass) {
        this.name = name;
        this.activityClass = activityClass;
    }

    public String getName() {
        return name;
    }

    Class getActivityClass() {
        return activityClass;
    }

    static ArrayList<Chart> createChartList(Resources resources) {
        ArrayList<Chart> chartList = new ArrayList<>();

        chartList.add(new Chart(resources.getString(R.string.pie_chart), PieChartActivity.class));
        chartList.add(new Chart(resources.getString(R.string.line_chart), LineChartActivity.class));
        chartList.add(new Chart(resources.getString(R.string.area_chart), AreaChartActivity.class));
        chartList.add(new Chart(resources.getString(R.string.area3d_chart), linechart.class));

        return chartList;
    }

}
