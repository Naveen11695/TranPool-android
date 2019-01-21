package naveen.hackathon.hackathon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class category extends AppCompatActivity {
    private static SharedPreferences sharedPreferences;
    private static final String Locale_KeyValue = "Saved Locale";
    private TextView message;
    protected static String user_type;
    private FirebaseAuth mAuth;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        message=findViewById(R.id.cat_mess);
        final Spinner spinner = (Spinner) findViewById(R.id.catagory);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.users, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        findViewById(R.id.proceed_catagory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    switch(spinner.getSelectedItemPosition())
                    {
                        case 0:
                            user_type="buyer";
                            startActivity(new Intent (getApplicationContext(),LoginActivity.class));
                            break;

                        case 1:
                            user_type="transporter";
                            startActivity(new Intent (getApplicationContext(),LoginActivity.class));
                            break;

                        case 2:
                            user_type="entrepreneur";
                            startActivity(new Intent (getApplicationContext(),LoginActivity.class));
                            break;
                        case 3:
                            user_type="Administrator";
                            startActivity(new Intent (getApplicationContext(),LoginActivity.class));
                            break;
                    }
            }
        });

        updateTexts();

    }

    public void loadLocale() {
        String language = sharedPreferences.getString(Locale_KeyValue, "");

    }

    //Update text methods
    private void updateTexts() {
        message.setText(R.string.category_text);
    }

}
