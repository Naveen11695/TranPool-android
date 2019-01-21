package naveen.hackathon.hackathon;

import android.app.ProgressDialog;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class payment_gateway extends AppCompatActivity {
    private TextView payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        payment=findViewById(R.id.payment);
        payment.setText(R.string.paying);

        final ProgressDialog dialog = ProgressDialog.show(payment_gateway.this, "", getString(R.string.paying),
                true);
        dialog.show();
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need anything here
            }

            public void onFinish() {
                dialog.dismiss();
            }
        }.start();
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
