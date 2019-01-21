package naveen.hackathon.hackathon.entrepreneur;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import naveen.hackathon.hackathon.R;
import naveen.hackathon.hackathon.SignupActivity;


public class entrepreneur_reg extends AppCompatActivity {

    private EditText company_name;
    private EditText owner_name;
    private EditText address;
    private EditText alt_address;
    private EditText email;
    private EditText phone;
    private EditText pan;
    private EditText aadhar;
    private EditText gstin;
    private EditText reg_id;
    private Button validity;
    private Spinner business_type;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseFirestore Firebasefirestore;
    private String email_string,date;
    private int mYear, mMonth, mDay;
    private Spinner state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.en_registeration);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        Firebasefirestore= FirebaseFirestore.getInstance();


        company_name = (EditText) findViewById(R.id.company_name);
        owner_name = (EditText) findViewById(R.id.owner_name);
        address = (EditText) findViewById(R.id.address);
        alt_address = (EditText) findViewById(R.id.alt_address);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        pan = (EditText) findViewById(R.id.pan);
        aadhar = (EditText) findViewById(R.id.aadhar);
        gstin = (EditText) findViewById(R.id.gstin);
        reg_id = (EditText) findViewById(R.id.reg_id);
        validity = (Button) findViewById(R.id.validity);
        state= findViewById(R.id.state);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.state, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setPrompt("Select Transportation Service:");
        state.setAdapter(adapter2);
        validity.setText(new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault()).format(new Date()));

        //.............................set_save_data................................................................//
        if (auth.getCurrentUser() != null) {
            email_string=auth.getCurrentUser().getEmail();
            read_data();
        }


        //.............................set_save_data................................................................//
        validity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(entrepreneur_reg.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                    date= String.valueOf(dayOfMonth)+"-"+String.valueOf(monthOfYear+1)+"-"+String.valueOf(year);
                                    validity.setText(date);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        business_type = (Spinner) findViewById(R.id.business_type);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);




        findViewById(R.id.en_register_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.en_register).setVisibility(View.GONE);
                register();
            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.business_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        business_type.setAdapter(adapter);
    }

    public void read_data() {
        DocumentReference doc = Firebasefirestore.collection("MASTER_TABLE").document(email_string);
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot data = task.getResult();
                if(data.exists())
                {
                    company_name.setText(data.getString("company_name"));
                    owner_name.setText(data.getString("owner_name"));
                    address.setText(data.getString("address"));
                    alt_address.setText(data.getString("alt_address"));
                    email.setText(email_string);
                    phone.setText(data.getString("phone"));
                    pan.setText(data.getString("pan"));
                    aadhar.setText(data.getString("aadhar"));
                    gstin.setText(data.getString("gstin"));
                    reg_id.setText(data.getString("reg_id"));
                    validity.setText(data.getString("validity"));
                    //business_type.setSelection();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("fireStore","Reading data from firestore Fail");
            }
        });
    }

    public void register(){
        DocumentReference params =Firebasefirestore.collection("MASTER_TABLE").document(email_string);
        params.update("company_name", company_name.getText().toString());
        params.update("owner_name", owner_name.getText().toString());
        params.update("State",state.getSelectedItem().toString());
        params.update("address", address.getText().toString());
        params.update("alt_address", alt_address.getText().toString());
        params.update("email", email.getText().toString());
        params.update("phone", phone.getText().toString());
        params.update("pan", pan.getText().toString());
        params.update("aadhar", aadhar.getText().toString());
        params.update("gstin", gstin.getText().toString());
        params.update("reg_id", reg_id.getText().toString());
        params.update("validity", date);
        params.update("business_type", business_type.getSelectedItem().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
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