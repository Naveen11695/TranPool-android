package naveen.hackathon.hackathon.entrepreneur;

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

public class Product_shipping_Activity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseFirestore Firebasefirestore;
    private FirebaseAuth mAuth;
    private EditText Entrepreneur_id, Product_name, Quantity, Pick_add, Drop_add, Buyer_name, Contact_no;
    private RadioGroup rgroup;
    private RadioButton radioButton;
    private Button Submit;
    private String categoryString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_shipping_);
        mAuth = FirebaseAuth.getInstance();
        Firebasefirestore = FirebaseFirestore.getInstance();
        Entrepreneur_id = (EditText) findViewById(R.id.entrepreneur_id);
        Product_name = (EditText) findViewById(R.id.product_name_1);
        Quantity = (EditText) findViewById(R.id.quantity_4);
        Pick_add = (EditText) findViewById(R.id.pick_add);
        Drop_add = (EditText) findViewById(R.id.drop_add);
        Buyer_name = (EditText) findViewById(R.id.buyer_name);
        Contact_no = (EditText) findViewById(R.id.contact_no);
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
        newData.put("Entrepreneur_id", Entrepreneur_id.getText().toString());
        newData.put("Product_name", Product_name.getText().toString());
        newData.put("Quantity", Quantity.getText().toString());
        newData.put("Pick_add", Pick_add.getText().toString());
        newData.put("Drop_add", Drop_add.getText().toString());
        newData.put("Buyer_name", Buyer_name.getText().toString());
        newData.put("Contact_no", Contact_no.getText().toString());
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
