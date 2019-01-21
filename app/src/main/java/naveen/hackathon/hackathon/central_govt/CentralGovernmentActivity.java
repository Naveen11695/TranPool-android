package naveen.hackathon.hackathon.central_govt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import naveen.hackathon.hackathon.R;
import naveen.hackathon.hackathon.broker.Broker_Reg_Activity;
import naveen.hackathon.hackathon.reports_charts.charts.AreaChartActivity;
import naveen.hackathon.hackathon.reports_charts.charts.Import;
import naveen.hackathon.hackathon.reports_charts.charts.LineChartActivity;
import naveen.hackathon.hackathon.reports_charts.charts.PieChartActivity;
import naveen.hackathon.hackathon.reports_charts.charts.linechart;

public class CentralGovernmentActivity extends AppCompatActivity implements View.OnClickListener{
     LinearLayout report1,report2,report3,report4,report5,broker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central_government);
        report1=(LinearLayout)findViewById(R.id.idReport1);
        report2=(LinearLayout)findViewById(R.id.idReport2);
        report3=(LinearLayout)findViewById(R.id.idReport3);
        report4=(LinearLayout)findViewById(R.id.idReport4);
        report5=(LinearLayout)findViewById(R.id.idReport5);
        broker=(LinearLayout)findViewById(R.id.idBroker);
        report1.setOnClickListener(this);
        report2.setOnClickListener(this);
        report3.setOnClickListener(this);
        report4.setOnClickListener(this);
        report5.setOnClickListener(this);
        broker.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.idReport1:
                 startActivity(new Intent(CentralGovernmentActivity.this,Import.class));
                break;
            case R.id.idReport2:
                startActivity(new Intent(CentralGovernmentActivity.this,PieChartActivity.class));
                break;
            case R.id.idReport3:
                startActivity(new Intent(CentralGovernmentActivity.this,AreaChartActivity.class));
                break;
            case R.id.idReport4:
                startActivity(new Intent(CentralGovernmentActivity.this,LineChartActivity.class));
                break;
            case R.id.idReport5:
                startActivity(new Intent(CentralGovernmentActivity.this,linechart.class));
                break;
            case R.id.idBroker:
                startActivity(new Intent(CentralGovernmentActivity.this,Broker_Reg_Activity.class));
                break;
        }
    }
}
