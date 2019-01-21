package naveen.hackathon.hackathon;

import android.app.ProgressDialog;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class buyer_transport_price extends AppCompatActivity {


    private FirebaseFirestore Firebasefirestore;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    private String base,hiretime,km_charges,helper_charges_first_floor,helper_charges_ground_floor,warehouse;
    private TextView base_charges,hire,helper_f,helper_g,km_charge,ware;
    private View view, price_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_transport_price);

        mAuth = FirebaseAuth.getInstance();
        base = hiretime = km_charges = helper_charges_first_floor = helper_charges_ground_floor = warehouse = " ";
        Firebasefirestore = FirebaseFirestore.getInstance();



        final Spinner spinner = (Spinner) findViewById(R.id.price_type);
        final Spinner spinner2 = (Spinner) findViewById(R.id.city_country);
        final Spinner spinner3 = (Spinner) findViewById(R.id.mode);
        final Spinner spinner4 = (Spinner) findViewById(R.id.transport);
        Button calculate =(Button) findViewById(R.id.price_cal);


        price_layout = findViewById(R.id.price_view);
        base_charges = (TextView) findViewById(R.id.base);
        hire = (TextView) findViewById(R.id.hiretime);
        helper_f = (TextView) findViewById(R.id.helper_first);
        helper_g = (TextView) findViewById(R.id.helper_ground);
        km_charge = (TextView) findViewById(R.id.km_charges);
        ware = (TextView) findViewById(R.id.warehouse);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.price_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setPrompt("Select Transportation Service:");
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItemPosition() == 1) {
                    spinner2.setVisibility(View.VISIBLE);
                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.city_array, android.R.layout.simple_spinner_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //spinner2.setPrompt("Select the State:");
                    spinner2.setAdapter(adapter2);
                }
                if (spinner.getSelectedItemPosition() == 2) {
                    spinner2.setVisibility(View.VISIBLE);
                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.country_array, android.R.layout.simple_spinner_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //spinner2.setPrompt("Select the Country:");
                    spinner2.setAdapter(adapter2);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItemPosition() ==2) {
                    spinner3.setVisibility(View.VISIBLE);
                    ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.mode_2, android.R.layout.simple_spinner_item);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //spinner3.setPrompt("Select the Mode to transportation:");
                    spinner3.setAdapter(adapter3);
                }
                if (spinner.getSelectedItemPosition() ==1) {
                    spinner3.setVisibility(View.VISIBLE);
                    ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.mode_1, android.R.layout.simple_spinner_item);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //spinner3.setPrompt("Select the Mode to transportation:");
                    spinner3.setAdapter(adapter3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner3.getSelectedItemPosition() == 1 && spinner.getSelectedItemPosition()==1) {
                    spinner4.setVisibility(View.VISIBLE);
                    ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.transport, android.R.layout.simple_spinner_item);
                    adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //spinner4.setPrompt("Select the transportation:");
                    spinner4.setAdapter(adapter4);
                }
                else
                {
                    spinner4.setVisibility(View.GONE);
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==1&&spinner3.getSelectedItemPosition()==1&&spinner4.getSelectedItemPosition()==1)
                {
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    read_price("VEHICLE_STANDARD_RATE","local_delhi_road_cycle");
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();

                }

                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==1&&spinner3.getSelectedItemPosition()==1&&spinner4.getSelectedItemPosition()==2)
                {
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    read_price("VEHICLE_STANDARD_RATE","local_delhi_road_bike");
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();

                }


                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==1&&spinner3.getSelectedItemPosition()==1&&spinner4.getSelectedItemPosition()==3)
                {
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    read_price("VEHICLE_STANDARD_RATE","local_delhi_road_car");
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();

                }



                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==1&&spinner3.getSelectedItemPosition()==1&&spinner4.getSelectedItemPosition()==4)
                {
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    read_price("VEHICLE_STANDARD_RATE","local_delhi_road_truck");
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();

                }


                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==2&&spinner3.getSelectedItemPosition()==1&&spinner4.getSelectedItemPosition()==1)
                {
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    read_price("VEHICLE_STANDARD_RATE","local_banglore_road_cycle");
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();

                }

                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==2&&spinner3.getSelectedItemPosition()==1&&spinner4.getSelectedItemPosition()==2)
                {
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    read_price("VEHICLE_STANDARD_RATE","local_banglore_road_bike");
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();

                }

                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==2&&spinner3.getSelectedItemPosition()==1&&spinner4.getSelectedItemPosition()==3)
                {
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    read_price("VEHICLE_STANDARD_RATE","local_banglore_road_car");
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();

                }


                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==2&&spinner3.getSelectedItemPosition()==1&&spinner4.getSelectedItemPosition()==4)
                {
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    read_price("VEHICLE_STANDARD_RATE","local_banglore_road_truck");
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();

                }


                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==3&&spinner3.getSelectedItemPosition()==1&&spinner4.getSelectedItemPosition()==4)
                {
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    read_price("VEHICLE_STANDARD_RATE","local_delhi_road_truck");
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();

                }


                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==3&&spinner3.getSelectedItemPosition()==1&&spinner4.getSelectedItemPosition()==1)
                {
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    read_price("VEHICLE_STANDARD_RATE","local_hyderabad_road_cycle");
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();

                }


                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==3&&spinner3.getSelectedItemPosition()==1&&spinner4.getSelectedItemPosition()==2)
                {
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    read_price("VEHICLE_STANDARD_RATE","local_hyderabad_road_bike");
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();

                }


                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==3&&spinner3.getSelectedItemPosition()==1&&spinner4.getSelectedItemPosition()==3)
                {
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    read_price("VEHICLE_STANDARD_RATE","local_hyderabad_road_car");
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();

                }


                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==3&&spinner3.getSelectedItemPosition()==1&&spinner4.getSelectedItemPosition()==4)
                {
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    read_price("VEHICLE_STANDARD_RATE","local_hyderabad_road_truck");
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();

                }




                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==1&&spinner3.getSelectedItemPosition()==2)
                {
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    read_price("VEHICLE_STANDARD_RATE","local_delhi_air");
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();

                }
                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==1&&spinner3.getSelectedItemPosition()==3)
                {
                    read_price("VEHICLE_STANDARD_RATE","local_dehi_train");

                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();
                }
                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==2&&spinner3.getSelectedItemPosition()==2)
                {
                    read_price("VEHICLE_STANDARD_RATE","local_bangalore_air");
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();
                }
                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==2&&spinner3.getSelectedItemPosition()==3)
                {
                    read_price("VEHICLE_STANDARD_RATE","local_bangalore_train");
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();
                }
                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==3&&spinner3.getSelectedItemPosition()==2)
                {
                    read_price("VEHICLE_STANDARD_RATE","local_hyderabad_air");
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();
                }
                if(spinner.getSelectedItemPosition()==1&&spinner2.getSelectedItemPosition()==3&&spinner3.getSelectedItemPosition()==3)
                {
                    read_price("VEHICLE_STANDARD_RATE","local_hyderabad_train");
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();
                }
                if(spinner.getSelectedItemPosition()==2&&spinner2.getSelectedItemPosition()==1&&spinner3.getSelectedItemPosition()==1)
                {
                    read_price("VEHICLE_STANDARD_RATE","global_china_air");
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();
                }if(spinner.getSelectedItemPosition()==2&&spinner2.getSelectedItemPosition()==1&&spinner3.getSelectedItemPosition()==2)
                {
                    read_price("VEHICLE_STANDARD_RATE","global_china_sea");
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();
                }
                if(spinner.getSelectedItemPosition()==2&&spinner2.getSelectedItemPosition()==2&&spinner3.getSelectedItemPosition()==1)
                {
                    read_price("VEHICLE_STANDARD_RATE","global_japan_air");
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();
                }
                if(spinner.getSelectedItemPosition()==2&&spinner2.getSelectedItemPosition()==2&&spinner3.getSelectedItemPosition()==2)
                {
                    read_price("VEHICLE_STANDARD_RATE","global_japan_sea");
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();
                }
                if(spinner.getSelectedItemPosition()==2&&spinner2.getSelectedItemPosition()==3&&spinner3.getSelectedItemPosition()==1)
                {
                    read_price("VEHICLE_STANDARD_RATE","global_srilanka_air");
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();
                }if(spinner.getSelectedItemPosition()==2&&spinner2.getSelectedItemPosition()==3&&spinner3.getSelectedItemPosition()==2)
                {
                    read_price("VEHICLE_STANDARD_RATE","global_srilanka_sea");
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();
                }


                if(spinner.getSelectedItemPosition()==2&&spinner2.getSelectedItemPosition()==1&&spinner3.getSelectedItemPosition()==2)
                {
                    read_price("VEHICLE_STANDARD_RATE","global_china_sea");
                    final ProgressDialog dialog = ProgressDialog.show(buyer_transport_price.this, "", "Detecting...",
                            true);
                    dialog.show();
                    new CountDownTimer(2000, 200) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need anything here
                        }

                        public void onFinish() {
                            dialog.dismiss();

                            Log.i("fireStore",base+" "+hiretime+" "+km_charges+" "+helper_charges_ground_floor+" "+helper_charges_first_floor+" "+warehouse);
                            update_price();
                        }
                    }.start();
                }


            }
        });





    }


    private void update_price() {
        price_layout.setVisibility(View.VISIBLE);
        Log.e("standard_price_details",base+" "+hiretime +" "+km_charges +" "+helper_charges_first_floor +" "+helper_charges_ground_floor +" "+warehouse);
        base_charges.setText(base);
        helper_f.setText(helper_charges_first_floor);
        helper_g.setText(helper_charges_ground_floor);
        hire.setText(hiretime);
        km_charge.setText(km_charges);
        ware.setText(warehouse);
    }



    public void read_price(String collection,String identifier) {
        DocumentReference doc = Firebasefirestore.collection(collection).document(identifier);
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot data = task.getResult();
                if(data.exists())
                {
                    base=data.getString("base_fare");
                    hiretime=data.getString("hiretime_charges");
                    km_charges=data.getString("km_charges");
                    helper_charges_first_floor=data.getString("helper_charges_first_floor");
                    helper_charges_ground_floor=data.getString("helper_charges_ground_floor");
                    warehouse=data.getString("warehouse_charges");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("fireStore","Reading data from firestore Fail");
            }
        });
    }



}
