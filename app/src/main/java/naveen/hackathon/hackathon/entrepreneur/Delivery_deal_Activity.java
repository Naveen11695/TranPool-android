package naveen.hackathon.hackathon.entrepreneur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import naveen.hackathon.hackathon.R;

public class Delivery_deal_Activity extends AppCompatActivity implements View.OnClickListener {
    private Button Deal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_deal_);
        Deal=(Button)findViewById(R.id.deal);
        Deal.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(Delivery_deal_Activity.this,Product_shipping_Activity.class));
    }
}
