package naveen.hackathon.hackathon;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import naveen.hackathon.hackathon.Transporter.Transporter_home;
import naveen.hackathon.hackathon.activities.ECartHomeActivity;
import naveen.hackathon.hackathon.entrepreneur.entrepreneur_home;


public class Splash extends AppCompatActivity {

        private ProgressBar mProgressBar;
        private int mProgressStatus = 0;
        TextView title;
        private FirebaseFirestore Firebasefirestore;
        private FirebaseAuth mAuth;
        private String email,firestore_user_type="null";

        private Handler mHandler = new Handler();

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.splash);

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        title=(TextView)findViewById(R.id.title);

        if(isNetworkAvailable()) {
            if (mAuth.getCurrentUser() != null) {
                email=mAuth.getCurrentUser().getEmail();
                read_firestore();
            }
            mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (mProgressStatus < 100){
                                mProgressStatus++;
                                android.os.SystemClock.sleep(50);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setProgress(mProgressStatus);

                                    }
                                });
                            }
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(isNetworkAvailable()) {
                                        onSplash();
                                    }

                                    else {
                                        Toast.makeText(getApplicationContext(),"Network Unavailable!!",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                        }
                    }).start();
                }

        else
        {
            View snackbarView = getWindow().getDecorView().getRootView();
            Snackbar.make(snackbarView, "Network Unavailable !", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(),Splash.class));
                            finish();
                        }
                    })
                    .show();

        }

    }

    private void read_firestore() {
        Firebasefirestore = FirebaseFirestore.getInstance();
        DocumentReference doc = Firebasefirestore.collection("MASTER_TABLE").document(email);
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot data = task.getResult();
                if(data.exists())
                {
                    firestore_user_type=data.getString("User Type");
                }
                Log.e("user_type",firestore_user_type);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("fireStore","Reading data from firestore Fail");
            }
        });
    }

    public void onSplash()
    {
        if(firestore_user_type.equalsIgnoreCase("buyer")) {
            startActivity(new Intent(getApplicationContext(), ECartHomeActivity.class));
            finish();
        }
        else if(firestore_user_type.equalsIgnoreCase("transporter")) {
            startActivity(new Intent(getApplicationContext(), Transporter_home.class));
            finish();
        }
        else if(firestore_user_type.equalsIgnoreCase("entrepreneur")) {
            startActivity(new Intent(getApplicationContext(), entrepreneur_home.class));
            finish();
        }
        else {
            startActivity(new Intent(getApplicationContext(), language.class));
            finish();
        }
    }


    // Private class isNetworkAvailable
    private boolean isNetworkAvailable() {
        // Using ConnectivityManager to check for Network Connection
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }




}


