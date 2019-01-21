package naveen.hackathon.hackathon.entrepreneur;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import naveen.hackathon.hackathon.R;
import naveen.hackathon.hackathon.SignupActivity;

public class product_description extends AppCompatActivity {

    private EditText product_name, short_des, long_des, discount, quantity;
    private TextView mrp, sell_price;
    private int selling_price;
    private FirebaseFirestore Firebasefirestore;
    private FirebaseAuth mAuth;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_description);
        mAuth = FirebaseAuth.getInstance();
        Firebasefirestore = FirebaseFirestore.getInstance();
        product_name = findViewById(R.id.pro_name);
        short_des = findViewById(R.id.short_des);
        long_des = findViewById(R.id.long_des);
        discount = findViewById(R.id.discount);
        quantity = findViewById(R.id.quantity);
        mrp = findViewById(R.id.mrp);
        sell_price = findViewById(R.id.sell_price);
        entrepreneur_tosell obj = new entrepreneur_tosell();
        mrp.setText(String.valueOf(obj.total));

        quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!(discount.getText().toString().isEmpty())) {
                    selling_price = Integer.parseInt(mrp.getText().toString()) - (Integer.parseInt(mrp.getText().toString()) * Integer.parseInt(discount.getText().toString()) / 100);
                    sell_price.setText(String.valueOf(selling_price));
                } else {
                    selling_price = 0;
                    sell_price.setText(String.valueOf(selling_price));
                }
            }
        });
        findViewById(R.id.product_register_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


    }

    public void register() {
        Map<String, Object> newData = new HashMap<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            newData.put("Product Name", product_name.getText().toString());
            newData.put("Short Description", short_des.getText().toString());
            newData.put("Long Description", long_des.getText().toString());
            newData.put("Discount", discount.getText().toString());
            newData.put("Quantity", quantity.getText().toString());
            newData.put("Selling Price", sell_price.getText().toString());
            newData.put("Product Category", bundle.getString("Product Category"));
            newData.put("Raw", bundle.getString("Raw"));
            newData.put("Quality", bundle.getString("Quality"));
            newData.put("Quantity", bundle.getString("Quantity"));
            newData.put("Cost", bundle.getString("Cost"));
            newData.put("Total Cost", bundle.getString("Total Cost"));
            newData.put("Transport Used", bundle.getString("Transport Used"));
            newData.put("Distance(Near by Store)", bundle.getString("Distance(Near by Store)"));
            newData.put("Transportation Cost", bundle.getString("Transportation Cost"));
            newData.put("Pre-processing Cost", bundle.getString("Pre-processing Cost"));
            newData.put("Processing Cost",bundle.getString("Processing Cost"));
            newData.put("Packing Cost", bundle.getString("Packing Cost"));
            newData.put("Post Processing Cost", bundle.getString("Post Processing Cost"));
            newData.put("Expected Cost", bundle.getString("Expected Cost"));
            newData.put("Profit margin", bundle.getString("Profit margin"));
            newData.put("Total Price", bundle.getString("Total Price"));
        }
        Firebasefirestore.collection(bundle.getString("Product Category")).document(product_name.getText().toString())
                .set(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
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
