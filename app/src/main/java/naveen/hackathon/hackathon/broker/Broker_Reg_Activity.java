package naveen.hackathon.hackathon.broker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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
import naveen.hackathon.hackathon.entrepreneur.gridAdapter;

public class Broker_Reg_Activity extends AppCompatActivity {
    FirebaseAuth auth;
    ArrayList<String> Area,Vehicle,area_operation;
    Spinner gridView;
    gridAdapter adapter;
    public static Activity activity;
    Button register;
    private FirebaseFirestore Firebasefirestore;
    EditText bname,passport,aadhar;
    Spinner area,area_operations,vehicle;
    private String email_string;
    @SuppressLint({"CutPasteId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker__reg_);

        auth = FirebaseAuth.getInstance();
        Firebasefirestore= FirebaseFirestore.getInstance();

        Area = new ArrayList<String>();
        area=findViewById(R.id.area);
        area_operations=findViewById(R.id.area_of_operation);
        vehicle=findViewById(R.id.vehicle);

        activity = this;

        bname=(EditText)findViewById(R.id.broker_name);
        passport=(EditText)findViewById(R.id.passport);
        aadhar=(EditText)findViewById(R.id.aadhar_broker);
        area=(Spinner)findViewById(R.id.area);
        area_operations=(Spinner)findViewById(R.id.area_of_operation);
        vehicle=(Spinner)findViewById(R.id.vehicle);
        register=(Button)findViewById(R.id.en_register_bt);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.area, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner3.setPrompt("Select the Mode to transportation:");
        area.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.area_op, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner3.setPrompt("Select the Mode to transportation:");
        area_operations.setAdapter(adapter2);


        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.vehicle_b_list, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner3.setPrompt("Select the Mode to transportation:");
        vehicle.setAdapter(adapter3);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeintodatabase(view);
            }
        });
    }
    public void writeintodatabase(View v){
        Map<String, Object> params = new HashMap<>();
        //DocumentReference params =Firebasefirestore.collection("MST_BROKER").document();
        params.put("Broker Name", bname.getText().toString());
        params.put("passport", passport.getText().toString());
        params.put("aadhar", aadhar.getText().toString());
        params.put("area", area.getSelectedItem().toString());
        params.put("area_operation", area_operations.getSelectedItem().toString());
        params.put("vehicle", vehicle.getSelectedItem().toString());
        Firebasefirestore.collection("MST_BROKER").document("broker").set(params).addOnSuccessListener(new OnSuccessListener<Void>() {
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
}
