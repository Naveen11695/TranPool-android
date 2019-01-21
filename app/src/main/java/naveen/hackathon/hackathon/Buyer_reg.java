package naveen.hackathon.hackathon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import naveen.hackathon.hackathon.R;

public class Buyer_reg extends AppCompatActivity {

    private EditText firm_name;
    private EditText owner_name;
    private EditText country;
    private EditText state;
    private EditText phone;
    private EditText pan;
    private EditText aadhar;
    private EditText address;
    private EditText email;
    private EditText website;
    private RadioGroup category;
    private String categoryString = "";
    private FirebaseAuth auth;
    private String email_string;
    private FirebaseFirestore Firebasefirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        Firebasefirestore= FirebaseFirestore.getInstance();


        firm_name = (EditText) findViewById(R.id.firm_name);
        owner_name = (EditText) findViewById(R.id.owner_name);
        country = (EditText) findViewById(R.id.country);
        state = (EditText) findViewById(R.id.state);
        phone = (EditText) findViewById(R.id.phone);
        pan = (EditText) findViewById(R.id.pan);
        aadhar = (EditText) findViewById(R.id.aadhar);
        address = (EditText) findViewById(R.id.address);
        email = (EditText) findViewById(R.id.email);
        website = (EditText) findViewById(R.id.website);
        category = (RadioGroup) findViewById(R.id.category);
        email_string=auth.getCurrentUser().getEmail();
    }

    public void register(View v){

        DocumentReference params =Firebasefirestore.collection("MASTER_TABLE").document(email_string);
        params.update("firm_name", firm_name.getText().toString());
        params.update("owner_name", owner_name.getText().toString());
        params.update("country", country.getText().toString());
        params.update("state", state.getText().toString());
        params.update("phone", phone.getText().toString());
        params.update("pan", pan.getText().toString());
        params.update("aadhar", aadhar.getText().toString());
        params.update("address", address.getText().toString());
        params.update("email", email.getText().toString());
        params.update("website", website.getText().toString());
        params.update("category", categoryString).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("firestore update","Success");
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("firestore update","fail");
                finish();
            }
        });
        Log.i("Params", params.toString());
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_company:
                if (checked)
                    categoryString = "Company";
                    break;
            case R.id.radio_goventment:
                if (checked)
                    categoryString = "Government";
                    break;
            case R.id.radio_individual:
                if (checked)
                    categoryString = "Individual";
                    break;
        }
    }
}