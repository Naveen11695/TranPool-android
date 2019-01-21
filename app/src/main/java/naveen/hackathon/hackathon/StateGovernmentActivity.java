package naveen.hackathon.hackathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import naveen.hackathon.hackathon.reports_charts.charts.vehicle;

public class StateGovernmentActivity extends AppCompatActivity {
    LinearLayout report1,report2,report3,report4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_government);
        report1=(LinearLayout)findViewById(R.id.idReport1);
        report2=(LinearLayout)findViewById(R.id.idReport2);
        report3=(LinearLayout)findViewById(R.id.idReport3);
        report4=(LinearLayout)findViewById(R.id.idReport4);

    }

    public void click(View view) {
        switch(view.getId())
        {
            case R.id.idReport1:
                startActivity(new Intent(StateGovernmentActivity.this,vehicle.class));
                break;
            case R.id.idReport2:
                startActivity(new Intent(StateGovernmentActivity.this,vehicle.class));
                break;
            case R.id.idReport3:
                startActivity(new Intent(StateGovernmentActivity.this,vehicle.class));
                break;
            case R.id.idReport4:
                startActivity(new Intent(StateGovernmentActivity.this,vehicle.class));
                break;
        }
    }
}
