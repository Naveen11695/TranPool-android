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

import naveen.hackathon.hackathon.R;
import naveen.hackathon.hackathon.SignupActivity;

public class Transporter_reg extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore Firebasefirestore;
    EditText transporter_name;
    EditText phone;
    EditText email;
    EditText aadhar;
    private RadioGroup rgroup;
    private RadioButton radioButton;
    EditText pan;
    EditText address;
    Spinner transporter_type;
    Spinner transportation_type;
    Spinner transport_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.transporter_reg);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        Firebasefirestore= FirebaseFirestore.getInstance();
        transporter_name = (EditText) findViewById(R.id.transporter_name);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        aadhar = (EditText) findViewById(R.id.aadhar);
        pan = (EditText) findViewById(R.id.pan);
        address = (EditText) findViewById(R.id.address);


        transporter_type = (Spinner) findViewById(R.id.transporter_type);

        ArrayAdapter<CharSequence> transporter_type_adapter = ArrayAdapter.createFromResource(this, R.array.transporter_type, android.R.layout.simple_spinner_item);
        transporter_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transporter_type.setAdapter(transporter_type_adapter);

        transportation_type = (Spinner) findViewById(R.id.transportation_type);

        ArrayAdapter<CharSequence> transportation_type_adapter = ArrayAdapter.createFromResource(this, R.array.transportation_type, android.R.layout.simple_spinner_item);
        transportation_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transportation_type.setAdapter(transportation_type_adapter);

        transport_type = (Spinner) findViewById(R.id.transport_type);

        ArrayAdapter<CharSequence> transport_type_adapter = ArrayAdapter.createFromResource(this, R.array.transport_type, android.R.layout.simple_spinner_item);
        transport_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transport_type.setAdapter(transport_type_adapter);
    }

    public void register(View v){

        DocumentReference params =Firebasefirestore.collection("MASTER_TABLE").document(auth.getCurrentUser().getEmail());
        params.update("transporter_name", transporter_name.getText().toString());
        params.update("phone", phone.getText().toString());
        params.update("email", email.getText().toString());
        params.update("aadhar", aadhar.getText().toString());
        params.update("pan", pan.getText().toString());
        params.update("address", address.getText().toString());
        params.update("pan", pan.getText().toString());
        params.update("aadhar", aadhar.getText().toString());
        params.update("transporter_type", transporter_type.getSelectedItem().toString());
        params.update("transportation_type", transportation_type.getSelectedItem().toString());
        params.update("transport_type", transport_type.getSelectedItem().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (auth.getCurrentUser() != null) {
                    Log.i("firestore update","Success");
                    finish();
                }
                else {
                    startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (auth.getCurrentUser() != null) {
                    Log.i("firestore update","fail");
                    finish();
                }
                else {
                    startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                }
    }

});
    }

}

