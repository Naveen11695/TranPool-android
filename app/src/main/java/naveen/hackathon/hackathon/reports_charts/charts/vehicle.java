package naveen.hackathon.hackathon.reports_charts.charts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.EnumsAlign;
import com.anychart.anychart.LegendLayout;
import com.anychart.anychart.Pie;
import com.anychart.anychart.ValueDataEntry;
import com.anychart.anychart.chart.common.Event;
import com.anychart.anychart.chart.common.ListenersInterface;

import java.util.ArrayList;
import java.util.List;

import naveen.hackathon.hackathon.R;

public class vehicle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_common);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);

        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(vehicle.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Belagavi Devision", 1547));
        data.add(new ValueDataEntry("Bengaluru Devision", 789));
        data.add(new ValueDataEntry("Kalaburegi Devision", 7216));
        data.add(new ValueDataEntry("Mysuru Devision", 1486));

        pie.setData(data);

        pie.setTitle("Karnataka Vehicle Registration ");

        pie.getLabels().setPosition("outside");

        pie.getLegend().getTitle().setEnabled(true);
        pie.getLegend().getTitle()
                .setText("March")
                .setPadding(0d, 0d, 10d, 0d);

        pie.getLegend()
                .setPosition("center-bottom")
                .setItemsLayout(LegendLayout.HORIZONTAL)
                .setAlign(EnumsAlign.CENTER);

        anyChartView.setChart(pie);
    }
}