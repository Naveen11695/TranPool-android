package naveen.hackathon.hackathon;

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
import android.widget.Toast;

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

import naveen.hackathon.hackathon.activities.ECartHomeActivity;

public class location extends AppCompatActivity {

    private EditText drop_location;
    private FirebaseFirestore Firebasefirestore;
    private FirebaseAuth mAuth;
    private Spinner nation,location,transportation;
    private TextView price,discount,product_name,estimation_time;
    private Double Latitude,Longitude;
    private Double distance;
    private String longdir;
    private String latdir;
    private int transportation_cost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        mAuth = FirebaseAuth.getInstance();
        Firebasefirestore = FirebaseFirestore.getInstance();
        drop_location=findViewById(R.id.drop_loc);
        nation=findViewById(R.id.nation);
        location=findViewById(R.id.location);
        price=findViewById(R.id.price);
        product_name=findViewById(R.id.product_name);
        discount=findViewById(R.id.discount);
        estimation_time=findViewById(R.id.estimate_delivery);
        transportation=findViewById(R.id.transportation_use);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.permit, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setPrompt("Select Transportation Service:");
        nation.setAdapter(adapter1);
        nation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(nation.getSelectedItem().toString().equalsIgnoreCase("National")) {
                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.state, android.R.layout.simple_spinner_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //spinner.setPrompt("Select Transportation Service:");
                    location.setAdapter(adapter2);
                    location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(location.getSelectedItemPosition()!=0) {
                                read_loc(location.getSelectedItem().toString());
                                transportation.setSelection(0);
                                final ProgressDialog dialog = ProgressDialog.show(location.this, "", "Detecting...",
                                        true);
                                dialog.show();
                                new CountDownTimer(2000, 300) {

                                    public void onTick(long millisUntilFinished) {
                                        // You don't need anything here
                                    }

                                    public void onFinish() {
                                        dialog.dismiss();

                                        Log.i("Longitude and Latitude", String.valueOf(Longitude)+" "+String.valueOf(Latitude));
                                    }
                                }.start();
                            }
                            }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.transport_type1, android.R.layout.simple_spinner_item);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //spinner.setPrompt("Select Transportation Service:");
                    transportation.setAdapter(adapter3);

                    transportation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(transportation.getSelectedItemPosition()!=0) {
                                list_loc(location.getSelectedItem().toString());
                                Distance obj=new Distance();
                                transportation_cost=obj.price(distance);
                                calculate_cost();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                }
                else if(nation.getSelectedItem().toString().equalsIgnoreCase("International")) {
                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.country, android.R.layout.simple_spinner_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //spinner.setPrompt("Select Transportation Service:");
                    location.setAdapter(adapter2);

                    ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.transport_type2, android.R.layout.simple_spinner_item);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //spinner.setPrompt("Select Transportation Service:");
                    transportation.setAdapter(adapter3);

                    transportation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(transportation.getSelectedItemPosition()!=0) {
                                Distance obj=new Distance();
                                transportation_cost=obj.price1(distance);
                                calculate_cost();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!price.getText().toString().isEmpty()) {
                    write_loc();
                    startActivity(new Intent(getApplicationContext(),payment_gateway.class));
                    finish();
                }
            }
        });


    }

    private void list_loc(String location) {
        DocumentReference doc = Firebasefirestore.collection("MST_COUNTRY").document(location);
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot data = task.getResult();
                if(data.exists())
                {
                    Longitude= Double.valueOf(data.getString("Longitude"));
                    Latitude= Double.valueOf(data.getString("Latitude"));
                    longdir= (data.getString("LongDir"));
                    latdir=  (data.getString("LatDir"));
                    Distance obj=new Distance(Longitude,Latitude,longdir,latdir);
                    Distance obj1=new Distance(77,28,"N","E");
                    distance=obj.diffCountry(obj1);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("fireStore","Reading data from firestore Fail");
            }
        });

    }

    private void calculate_cost() {
        StringBuffer product_list,discount_list;
        int final_price=0;
        if(nation.getSelectedItemPosition()!=0&&location.getSelectedItemPosition()!=0) {
            product_list = new StringBuffer();
            discount_list= new StringBuffer();
            for(int i=0;i<ECartHomeActivity.productId.size();i++) {
                Log.e("Product_list:",ECartHomeActivity.productId.get(i));
                if(i==ECartHomeActivity.productId.size()-1){
                    product_list.append(ECartHomeActivity.productId.get(i));
                    discount_list.append(ECartHomeActivity.productdiscount);
                    final_price+=Integer.parseInt(ECartHomeActivity.productprice.get(i));
                }
                else {
                    product_list.append(ECartHomeActivity.productId.get(i)+" , ");
                    discount_list.append(ECartHomeActivity.productdiscount);
                    final_price=Integer.parseInt(ECartHomeActivity.productprice.get(i));
                }

            }
            final_price=0;
            for(int i=0;i<ECartHomeActivity.productId.size();i++) {
                final_price+=Integer.parseInt(ECartHomeActivity.productprice.get(i));
            }

            product_name.setText(product_list);
            if(final_price>10000)
            {
                discount.setText("20 %");
            //    Log.e("Distance", String.valueOf(obj.price(distance)));
             //   Toast.makeText(getApplicationContext(),String.valueOf(obj.price(distance)),Toast.LENGTH_LONG).show();
                price.setText(String.valueOf(final_price-(final_price/20)+transportation_cost));
            }
            else
            {
                discount.setText("5 %");
             //   Toast.makeText(getApplicationContext(),String.valueOf(obj.price(distance)),Toast.LENGTH_LONG).show();
                price.setText(String.valueOf(final_price-(final_price/5)+transportation_cost));
            }

            if(nation.getSelectedItem().toString().equalsIgnoreCase("National")) {
                estimation_time.setText(R.string.estimation_delivery1);
            }
            else if(nation.getSelectedItem().toString().equalsIgnoreCase("International")){
                estimation_time.setText(R.string.estimation_delivery2);
            }
        }


    }

    private void write_loc() {
        Map<String, Object> newData = new HashMap<>();
            newData.put("User Type", "buyer");
            newData.put("Product name",product_name.getText().toString());
            newData.put("Price",price.getText().toString());
            newData.put("Discount",discount.getText().toString());
            newData.put("Address",drop_location.getText().toString());
            newData.put("Distance",distance.toString());
            newData.put("LOCATION TYPE_!",nation.getSelectedItem().toString());
            newData.put("LOCATION_TYPE_2",location.getSelectedItem().toString());
            newData.put("Transport Type",transportation.getSelectedItem().toString());
            newData.put("EST. delivery Time",estimation_time.getText().toString());

            Firebasefirestore.collection("ORDER_INFO").document(mAuth.getCurrentUser().getEmail())
                    .set(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
    }


    public void read_loc(String location) {

        DocumentReference doc = Firebasefirestore.collection("MST_INDIA").document(location);
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot data = task.getResult();
                if(data.exists())
                {
                    Longitude= Double.valueOf(data.getString("Longitude"));
                    Latitude= Double.valueOf(data.getString("Latitude"));
                    Distance obj=new Distance(Longitude,Latitude);
                    Distance obj1=new Distance(75.7138,15.3172);
                    distance=obj.sameCountry(obj1);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("fireStore","Reading data from firestore Fail");
            }
        });

        Toast.makeText(getApplicationContext(),"Order is successfully Placed.",Toast.LENGTH_LONG).show();

    }


       /* ///////////////////////////////////.......................Query............................/////////////////////////////////////

        Task<QuerySnapshot> doc = Firebasefirestore.collection("MST_INDIA")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Result", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("Result", "Error getting documents.", task.getException());
                        }
                    }
                });


        ///////////////////////////////////.......................Query............................/////////////////////////////////////*/


}
