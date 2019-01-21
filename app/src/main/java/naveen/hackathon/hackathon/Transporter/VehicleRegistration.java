package naveen.hackathon.hackathon.Transporter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import naveen.hackathon.hackathon.R;
import naveen.hackathon.hackathon.SignupActivity;

public class VehicleRegistration extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseFirestore Firebasefirestore;
    EditText vehicle_name;
    EditText vehicle_number;
    Spinner transport_type;
    Spinner permit;
    EditText permit_number;
    RadioGroup tax_status;
    EditText tax_no;
    RadioGroup fitness_cert_status;
    RadioGroup vehicle_type;
    RadioGroup have_ac;
    EditText max_capacity;

    String string_tax_paid;
    String string_fitness_cert;
    String string_perishable;
    String string_have_ac;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_reg);

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        Firebasefirestore = FirebaseFirestore.getInstance();

        vehicle_name = (EditText) findViewById(R.id.vehicle_name);

        vehicle_number = (EditText) findViewById(R.id.vehicle_number);

        transport_type = (Spinner) findViewById(R.id.transport_type);

        ArrayAdapter<CharSequence> transport_type_adapter = ArrayAdapter.createFromResource(this, R.array.transport_type, android.R.layout.simple_spinner_item);
        transport_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transport_type.setAdapter(transport_type_adapter);

        permit = (Spinner) findViewById(R.id.permit);

        ArrayAdapter<CharSequence> permit_adapter = ArrayAdapter.createFromResource(this, R.array.permit, android.R.layout.simple_spinner_item);
        permit_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        permit.setAdapter(permit_adapter);

        permit_number = (EditText) findViewById(R.id.permit_number);

        tax_status = (RadioGroup) findViewById(R.id.tax_status);

        tax_no = (EditText) findViewById(R.id.tax_no);

        fitness_cert_status = (RadioGroup) findViewById(R.id.fitness_cert_status);

        vehicle_type = (RadioGroup) findViewById(R.id.vehicle_type);

        have_ac = (RadioGroup) findViewById(R.id.have_ac);

        max_capacity = (EditText) findViewById(R.id.max_capacity);
    }


    public void onTaxRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radio_tax_paid_yes:
                if (checked)
                    string_tax_paid = "Yes";
                break;
            case R.id.radio_tax_paid_no:
                if (checked)
                    string_tax_paid = "No";
                break;
        }
    }

    public void onFitnessRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radio_fitness_cert_yes:
                if (checked)
                    string_fitness_cert = "Yes";
                break;
            case R.id.radio_fitness_cert_no:
                if (checked)
                    string_fitness_cert = "No";
                break;
        }
    }

    public void onVehicleTypeRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radio_perishable_yes:
                if (checked)
                    string_perishable = "Yes";
                break;
            case R.id.radio_perishable_no:
                if (checked)
                    string_perishable = "No";
                break;
        }
    }

    public void onACRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radio_have_ac_yes:
                if (checked)
                    string_have_ac = "Yes";
                break;
            case R.id.radio_have_ac_no:
                if (checked)
                    string_have_ac = "No";
                break;
        }
    }

    public void register(View v) {


        Map<String, Object> params = new HashMap<>();
        params.put("vehicle_name", vehicle_name.getText().toString());
        params.put("vehicle_number", vehicle_number.getText().toString());
        params.put("transport_type", transport_type.getSelectedItem().toString());
        params.put("permit", permit.getSelectedItem().toString());
        params.put("permit_number", permit_number.getText().toString());
        params.put("tax_paid", string_tax_paid);
        params.put("tax_no", tax_no.getText().toString());
        params.put("fitness_cert", string_fitness_cert);
        params.put("vehicle_type", string_perishable);
        params.put("have_ac", string_have_ac);
        Firebasefirestore.collection("VEHICLE_REGISTRATION").document(mAuth.getCurrentUser().getEmail())
                .set(params).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (mAuth.getCurrentUser() != null) {
                    Log.i("firestore update", "Success");
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (mAuth.getCurrentUser() != null) {
                    Log.i("firestore update", "fail");
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                }

            }
        });
    }
}