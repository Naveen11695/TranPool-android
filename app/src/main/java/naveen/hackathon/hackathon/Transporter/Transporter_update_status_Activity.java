package naveen.hackathon.hackathon.Transporter;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import naveen.hackathon.hackathon.R;

public class Transporter_update_status_Activity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore Firebasefirestore;
    private FirebaseAuth mAuth;
    private EditText State, Start_loc, End_loc, Route, Transport_name, Capacity, Availability, Charge, Current_order;
    private RadioGroup rgroup;
    private RadioButton radioButton;
    private Button Submit;
    private String categoryString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transporter_update_status_);
        mAuth = FirebaseAuth.getInstance();
        Firebasefirestore = FirebaseFirestore.getInstance();
        State = (EditText) findViewById(R.id.state);
        Start_loc = (EditText) findViewById(R.id.start_loc);
        End_loc = (EditText) findViewById(R.id.end_loc);
        Route = (EditText) findViewById(R.id.route);
        Transport_name = (EditText) findViewById(R.id.transport_name);
        Capacity = (EditText) findViewById(R.id.capacity);
        Availability = (EditText) findViewById(R.id.availability);
        Charge = (EditText) findViewById(R.id.charge);
        Current_order = (EditText) findViewById(R.id.current_order);
        rgroup = (RadioGroup) findViewById(R.id.perishable);
        Submit = (Button) findViewById(R.id.submit);
        Submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Write_to_firestore();
    }

    private void Write_to_firestore() {
        Map<String, Object> newData = new HashMap<>();
        newData.put("State", State.getText().toString());
        newData.put("Start_loc", Start_loc.getText().toString());
        newData.put("End_loc", End_loc.getText().toString());
        newData.put("Route", Route.getText().toString());
        newData.put("Transport_name", Transport_name.getText().toString());
        newData.put("Capacity", Capacity.getText().toString());
        newData.put("Availability", Availability.getText().toString());
        newData.put("Charge", Charge.getText().toString());
        newData.put("Current_order", Current_order.getText().toString());
        newData.put("Perishable", categoryString);
        Firebasefirestore.collection("Transporter_update_status").document(mAuth.getCurrentUser().getEmail())
                .set(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("Upload", "Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Upload", "Failed");
            }
        });
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.perishable_yes:
                if (checked)
                    categoryString = "yes";
                break;
            case R.id.perishable_no:
                if (checked)
                    categoryString = "no";
                break;
        }
    }
}
