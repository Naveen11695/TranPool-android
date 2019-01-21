package naveen.hackathon.hackathon.entrepreneur;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import naveen.hackathon.hackathon.R;
import naveen.hackathon.hackathon.SignupActivity;
import naveen.hackathon.hackathon.Transporter.Transporter_home;
import naveen.hackathon.hackathon.Transporter.WebActivity;

public class entrepreneur_group extends AppCompatActivity implements View.OnClickListener {
     CheckBox ch1,ch2,ch3,ch4;
     Button Submit;
    StringBuffer result;
    private FirebaseAuth auth;
    private FirebaseFirestore Firebasefirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        Firebasefirestore= FirebaseFirestore.getInstance();
        setContentView(R.layout.entrepreneur_group);
        ch1=(CheckBox)findViewById(R.id.checkBox2);
        ch2=(CheckBox)findViewById(R.id.checkBox);
        ch3=(CheckBox)findViewById(R.id.checkBox3);
        ch4=(CheckBox)findViewById(R.id.checkBox4);
        Submit=(Button)findViewById(R.id.Sub);
        Submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        result = new StringBuffer();
        if(ch1.isChecked())
        result.append("\nGroup1 Joined ");
        if(ch2.isChecked())
        result.append("\nGroup2 Joined");
        if(ch3.isChecked())
        result.append("\nGroup3 Joined ");
        if(ch4.isChecked())
        result.append("\nGroup4 Joined ");

        Toast.makeText(entrepreneur_group.this, result.toString(),Toast.LENGTH_LONG).show();
        databasewrite();
    }

    private void databasewrite() {
        Map<String, Object> newData = new HashMap<>();
        newData.put("User Group",result.toString());
        Firebasefirestore.collection("entrepreneur_group").document(auth.getCurrentUser().getEmail())
                .set(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

}
