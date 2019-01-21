package naveen.hackathon.hackathon;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

import naveen.hackathon.hackathon.activities.ECartHomeActivity;

public class language extends AppCompatActivity implements View.OnClickListener {
    private static Button english, hindi, kannada;
    private static TextView chooseText;
    private static Locale myLocale;
    private Button proceed;
    private String value;
    //Shared Preferences Variables
    private static final String Locale_Preference = "Locale Preference";
    private static final String Locale_KeyValue = "Saved Locale";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language);
        initViews();
        setListeners();
        loadLocale();
    }


    //Initiate all views
    private void initViews() {
        sharedPreferences = getSharedPreferences(Locale_Preference, Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        chooseText = (TextView) findViewById(R.id.choose_text);
        english = (Button) findViewById(R.id.english);
        hindi = (Button) findViewById(R.id.hindi);
        kannada = (Button) findViewById(R.id.kannada);
        proceed =(Button) findViewById(R.id.proceed);
    }

    //Set Click Listener
    private void setListeners() {
        english.setOnClickListener(this);
        hindi.setOnClickListener(this);
        kannada.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        String lang = "en";//Default Language
        switch (view.getId()) {
            case R.id.english:
                lang = "en";
                break;
            case R.id.hindi:
                lang = "hi";
                break;
            case R.id.kannada:
                lang = "kn";
                break;
        }

        changeLocale(lang);//Change Locale on selection basis
        updateTexts();//Update texts according to locale
    }


    //Change Locale
    public void changeLocale(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);//Set Selected Locale
        saveLocale(lang);//Save the selected locale
        Locale.setDefault(myLocale);//set new locale as default
        Configuration config = new Configuration();//get Configuration
        config.locale = myLocale;//set config locale as selected locale
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());//Update the config

    }

    //Save locale method in preferences
    public void saveLocale(String lang) {
        editor.putString(Locale_KeyValue, lang);
        editor.commit();
    }

    //Get locale method in preferences
    public void loadLocale() {
        String language = sharedPreferences.getString(Locale_KeyValue, "");
        changeLocale(language);

    }

    //Update text methods
    private void updateTexts() {
        chooseText.setText(R.string.tap_text);
        english.setText(R.string.btn_en);
        hindi.setText(R.string.btn_hi);
        kannada.setText(R.string.btn_ka);
        proceed.setText(R.string.proceed);

    }

    public void Proceed(View view) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            value = bundle.getString("key");
            assert value != null;
            if (value.equalsIgnoreCase("Buyer_class")) {
                startActivity(new Intent(getApplicationContext(), ECartHomeActivity.class));
                finish();
            }
        }
            else{
                startActivity(new Intent(getApplicationContext(),category.class));
            }
    }
}
