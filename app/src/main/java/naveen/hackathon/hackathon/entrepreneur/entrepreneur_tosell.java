package naveen.hackathon.hackathon.entrepreneur;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.internal.overlay.zzo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import naveen.hackathon.hackathon.R;
import naveen.hackathon.hackathon.SignupActivity;
import naveen.hackathon.hackathon.activities.ECartHomeActivity;
import naveen.hackathon.hackathon.buyer_transport_price;

public class entrepreneur_tosell extends AppCompatActivity {

    private FirebaseFirestore Firebasefirestore;
    private FirebaseAuth mAuth;
    private String base_fare;
    protected static int total_cost,transportation_cost,post_cost,total,pre_porcess_cost_cal;
    private Spinner spinner,spinner2,spinner3,spinner4,spinner5;
    private TextView Total_cost,trans_cost,post_process_cost,t_p,profit_margin,pre_processing_cost,expected_price;
    private EditText distance_cost,pre_cost,pack_cost;
    private EditText q1,q2,q3,c1,c2,c3,raw1,raw2,raw3;
    private DocumentReference doc;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrepreneur_tosell);
        mAuth = FirebaseAuth.getInstance();
        Firebasefirestore = FirebaseFirestore.getInstance();

        spinner = (Spinner) findViewById(R.id.product_select);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.PRODUCT_TYPE, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setPrompt("Select Transportation Service:");
        spinner.setAdapter(adapter);
        spinner2 = (Spinner) findViewById(R.id.qty_1);
        spinner3 = (Spinner) findViewById(R.id.qty2);
        spinner4 = (Spinner) findViewById(R.id.qty3);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.quality, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setPrompt("Select Transportation Service:");
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter2);
        spinner4.setAdapter(adapter2);

        q1=findViewById(R.id.quantity_1);
        q2=findViewById(R.id.quantity_2);
        q3=findViewById(R.id.quantity_3);
        c1=findViewById(R.id.cost_1);
        c2=findViewById(R.id.cost_2);
        c3=findViewById(R.id.cost_3);
        raw1=findViewById(R.id.raw_1);
        raw2=findViewById(R.id.raw_2);
        raw3=findViewById(R.id.raw_3);

        total_cost=0;
        findViewById(R.id.cal_cost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(c3.getText().length()==0)) {
                    total_cost= Integer.valueOf((q1.getText().toString()))*Integer.valueOf((c1.getText().toString()))
                            +   Integer.valueOf((q2.getText().toString()))*Integer.valueOf((c2.getText().toString()))
                            +   Integer.valueOf((q3.getText().toString()))*Integer.valueOf((c3.getText().toString()));
                    Total_cost=findViewById(R.id.total_cost);
                    Total_cost.setText("₹ "+total_cost);
                    Log.e("Cost", String.valueOf(total_cost));
                }
            }
        });


        spinner5 = (Spinner) findViewById(R.id.trans_use);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.transport_type, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setPrompt("Select Transportation Service:");
        spinner5.setAdapter(adapter3);
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner5.getSelectedItemPosition()!=0) {
                    read_trasport_type(spinner5.getSelectedItem().toString());
                    final ProgressDialog dialog = ProgressDialog.show(entrepreneur_tosell.this, "", "Detecting...",
                            true);
                    dialog.show();
                    new CountDownTimer(2000, 150) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();


                        }
                    }.start();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        distance_cost=findViewById(R.id.distance_cost);
        trans_cost=findViewById(R.id.trans_cost);
        pre_processing_cost=findViewById(R.id.total_pre_cost);

        findViewById(R.id.cal_cost2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(distance_cost.getText().toString().isEmpty()))
                {
                    transportation_cost= Integer.parseInt(base_fare)*Integer.parseInt(distance_cost.getText().toString());
                    trans_cost.setText(String.valueOf(transportation_cost));
                    pre_porcess_cost_cal=transportation_cost+total_cost;
                    pre_processing_cost.setText(String.valueOf(pre_porcess_cost_cal));
                }
            }
        });
        pre_cost=findViewById(R.id.pre_cost);
        pack_cost=findViewById(R.id.pack_cost);
        post_process_cost=findViewById(R.id.post_processing_cost);

        findViewById(R.id.cal_cost3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(pre_cost.getText().toString().isEmpty()&&pre_cost.getText().toString().isEmpty()))
                {
                    post_cost= Integer.parseInt(pre_cost.getText().toString())+Integer.parseInt(pack_cost.getText().toString());
                    post_process_cost.setText(String.valueOf(post_cost));
                }
            }
        });
        t_p=findViewById(R.id.total_price);
        expected_price=findViewById(R.id.exp_cost);

        profit_margin=findViewById(R.id.margin);
        findViewById(R.id.cal_cost4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total=(pre_porcess_cost_cal+post_cost);
                expected_price.setText(String.valueOf(total));
                total=(pre_porcess_cost_cal+post_cost)+((pre_porcess_cost_cal+post_cost)/10);
                t_p.setText(String.valueOf(total));
            }
        });


        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Write_to_firestore();
            }
        });

    }

    private void Write_to_firestore() {
        Intent intent = new Intent(entrepreneur_tosell.this, product_description.class);
        Bundle bundle = new Bundle();
        bundle.putString("Product Category",spinner.getSelectedItem().toString());
        bundle.putString("Raw",raw1.getText().toString()+"-"+raw2.getText().toString()+"-"+raw3.getText().toString());
        bundle.putString("Quality",spinner2.getSelectedItem().toString()+"-"+spinner3.getSelectedItem().toString()+"-"+spinner4.getSelectedItem().toString());
        bundle.putString("Quantity",q1.getText().toString()+"-"+q2.getText().toString()+"-"+q3.getText().toString());
        bundle.putString("Cost",c1.getText().toString()+"-"+c2.getText().toString()+"-"+c3.getText().toString());
        bundle.putString("Total Cost", String.valueOf(total_cost));
        bundle.putString("Transport Used",spinner5.getSelectedItem().toString());
        bundle.putString("Distance(Near by Store)",distance_cost.getText().toString());
        bundle.putString("Transportation Cost",trans_cost.getText().toString());
        bundle.putString("Pre-processing Cost", String.valueOf(pre_porcess_cost_cal));
        bundle.putString("Processing Cost",pre_cost.getText().toString());
        bundle.putString("Packing Cost",pack_cost.getText().toString());
        bundle.putString("Post Processing Cost", String.valueOf(post_cost));
        bundle.putString("Expected Cost",expected_price.getText().toString());
        bundle.putString("Profit margin",profit_margin.getText().toString());
        bundle.putString("Total Price", String.valueOf(total));
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                Log.e("sending","Successfully");

    }


    private void read_trasport_type(String type) {
        Log.e("Base fare",type);

        if(type.equalsIgnoreCase("cycle")) {
            doc = Firebasefirestore.collection("VEHICLE_STANDARD_RATE").document("local_banglore_road_cycle");
        }
        else if(type.equalsIgnoreCase("bike")) {
            doc = Firebasefirestore.collection("VEHICLE_STANDARD_RATE").document("local_banglore_road_bike");
        }
        else if(type.equalsIgnoreCase("car")) {
            doc = Firebasefirestore.collection("VEHICLE_STANDARD_RATE").document("local_banglore_road_car");
        }
        else if(type.equalsIgnoreCase("truck")) {
            doc = Firebasefirestore.collection("VEHICLE_STANDARD_RATE").document("local_banglore_road_truck");
        }

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot data = task.getResult();
                if(data.exists())
                {
                    base_fare=data.getString("km_charges");
                }
                Log.e("Base fare_price",base_fare);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("fireStore","Reading data from firestore Fail");
            }
        });
    }

}
