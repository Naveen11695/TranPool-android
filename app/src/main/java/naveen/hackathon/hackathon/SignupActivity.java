package naveen.hackathon.hackathon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import naveen.hackathon.hackathon.Transporter.Transporter_home;
import naveen.hackathon.hackathon.activities.ECartHomeActivity;
import naveen.hackathon.hackathon.central_govt.CentralGovernmentActivity;
import naveen.hackathon.hackathon.entrepreneur.entrepreneur_home;


public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseFirestore Firebasefirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        Firebasefirestore= FirebaseFirestore.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, forget_pass.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (!isEmailValid(email)) {
                    inputEmail.setError(getString(R.string.error_invalid_email));
                }

                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError(getString(R.string.error_field_required));
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    inputPassword.setError(getString(R.string.error_field_required));
                    return;
                }

                if (password.length() < 6) {
                    inputPassword.setError(getString(R.string.error_invalid_password));
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                findViewById(R.id.signin_layout).setVisibility(View.GONE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                            /*    Objects.requireNonNull(auth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.e("Event","Verification Email is sent");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("Event","Verification Email is not sent");
                                    }
                                });*/
                                progressBar.setVisibility(View.GONE);
                                final View v=findViewById(R.id.signin_layout);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Snackbar.make(v, "Email already exist. Please try different Email-Id.", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    findViewById(R.id.signin_layout).setVisibility(View.VISIBLE);
                                }
                                else {
                                    Map<String, Object> newData = new HashMap<>();
                                    if(category.user_type.equalsIgnoreCase("buyer"))
                                    {
                                        newData.put("User Type","buyer");
                                        newData.put("Email",email);
                                        Firebasefirestore.collection("MASTER_TABLE").document(email)
                                                .set(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent intent = new Intent(SignupActivity.this, ECartHomeActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("key", "value");
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(v, "Email registration failed. Please try different Email-Id.", Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();
                                                findViewById(R.id.signin_layout).setVisibility(View.VISIBLE);
                                            }
                                        });

                                    }
                                    else if(category.user_type.equalsIgnoreCase("transporter")) {
                                        newData.put("User Type","transporter");
                                        newData.put("Email",email);
                                        Firebasefirestore.collection("MASTER_TABLE").document(email)
                                                .set(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent intent = new Intent(SignupActivity.this, Transporter_home.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("key", "value");
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(v, "Email registration failed. Please try different Email-Id.", Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();
                                                findViewById(R.id.signin_layout).setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }
                                    else if(category.user_type.equalsIgnoreCase("entrepreneur")) {
                                        newData.put("User Type","entrepreneur");
                                        newData.put("Email",email);
                                        Firebasefirestore.collection("MASTER_TABLE").document(email)
                                                .set(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent intent = new Intent(SignupActivity.this, entrepreneur_home.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("key", "value");
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(v, "Email registration failed. Please try different Email-Id.", Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();
                                                findViewById(R.id.signin_layout).setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }
                                  /*  else if(category.user_type.equalsIgnoreCase("Administrator")) {
                                        newData.put("User Type","Administrator");
                                        newData.put("Email",email);
                                        Firebasefirestore.collection("MASTER_TABLE").document(email)
                                                .set(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent intent = new Intent(SignupActivity.this, CentralGovernmentActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("key", "value");
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(v, "Email registration failed. Please try different Email-Id.", Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();
                                                findViewById(R.id.signin_layout).setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }*/
                                }
                            }
                        });

            }
        });
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@gmail.com");
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}

