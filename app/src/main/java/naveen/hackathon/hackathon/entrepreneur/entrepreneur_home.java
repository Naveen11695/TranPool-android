package naveen.hackathon.hackathon.entrepreneur;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import naveen.hackathon.hackathon.About;
import naveen.hackathon.hackathon.R;
import naveen.hackathon.hackathon.Transporter.WebActivity;
import naveen.hackathon.hackathon.category;
import naveen.hackathon.hackathon.change_pass;
import naveen.hackathon.hackathon.feedback;
import naveen.hackathon.hackathon.language;
import naveen.hackathon.hackathon.reports_charts.charts.PieChartActivity;

public class entrepreneur_home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

     FirebaseAuth mAuth;
     FirebaseAuth.AuthStateListener mAuthListener;
     ArrayList<String> basicFields;
     GridView gridView;
    private TextView verified_Textviewer,email_Textviewer;
    private String email,username;
    private Intent j;
     gridAdapter adapter;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrepreneur_home);
        mAuth=FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth=FirebaseAuth.getInstance();
        email=mAuth.getCurrentUser().getEmail().toString();
        Intent j;


        basicFields = new ArrayList<String>();
        gridView = (GridView)findViewById(R.id.grid);
        basicFields.add("Personal Details");
        adapter = new gridAdapter(this,basicFields);
        //gridView.setAdapter(adapter);
        activity = this;


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_entrepreneur_home);
        email_Textviewer=(TextView)navHeaderView.findViewById(R.id.header_email);
        verified_Textviewer=(TextView)navHeaderView.findViewById(R.id.verified_status);
        update_indice();
    }

    @SuppressLint("SetTextI18n")
    private void update_indice() {
        if(!mAuth.getCurrentUser().isEmailVerified()) {
            email_Textviewer.setText(email);
            verified_Textviewer.setTextColor(Color.RED);
            verified_Textviewer.setText(R.string.verfiy_message1);
            View snackbarView = getWindow().getDecorView().getRootView();
            Snackbar.make(snackbarView, R.string.verfiy_message, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.verfication_button, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAuth.getCurrentUser().sendEmailVerification();
                        }
                    })
                    .show();
        }
        else {
            email_Textviewer.setText(email);
            verified_Textviewer.setTextColor(Color.GREEN);
            verified_Textviewer.setText(R.string.verfiy_message2);
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            moveTaskToBack (true);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_Home) {
            startActivity(new Intent(getApplicationContext(),entrepreneur_home.class));
            finish();
        }
        else if (id == R.id.Profile) {
            startActivity(new Intent(getApplicationContext(),entrepreneur_reg.class));
        }  else if (id == R.id.change_pass) {
            startActivity(new Intent(getApplicationContext(),change_pass.class));
        }  else if (id == R.id.tosell) {
            startActivity(new Intent(getApplicationContext(),entrepreneur_tosell.class));
        }  else if (id == R.id.product_shipping) {
            startActivity(new Intent(entrepreneur_home.this,Delivery_deal_Activity.class));
        }   else if (id == R.id.join_group) {
            startActivity(new Intent(getApplicationContext(),entrepreneur_group.class));
        }    else if (id == R.id.transporter_live_status) {
            startActivity(new Intent(getApplicationContext(),status.class));
        }   else if (id == R.id.weather) {
            startActivity(new Intent(getApplicationContext(),weather.class));
        }   else if (id == R.id.About_us) {
            startActivity(new Intent(getApplicationContext(),About.class));
        }   else if (id == R.id.feedback) {
            startActivity(new Intent(getApplicationContext(), feedback.class));
        }   else if (id == R.id.change_lan) {
            Intent intent=new Intent(getApplicationContext(),language.class);
            Bundle bundle = new Bundle();
            bundle.putString("key", "Buyer_class");
            intent.putExtras(bundle);
            startActivity(intent);
        }  else if (id == R.id.Log_out) {
            // Firebase sign out
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(),category.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.entre_menu,menu);
        return true;
    }





    public void entrepreneur_option(View view) {
        switch (view.getId()) {
            case R.id.idInsurance1: {
                j = new Intent(getApplicationContext(), WebActivity.class);
                j.putExtra("url", "https://bookatruck.in/insurance");
                startActivity(j);
            }
            break;
            case R.id.idInfo: {
                j = new Intent(getApplicationContext(), WebActivity.class);
                j.putExtra("url", "https://www.academia.edu/5450604/AN_OVERVIEW_OF_RURAL_ENTREPRENEURS_IN_INDIA_INTRODUCTION");
                startActivity(j);
            }
            break;
            case R.id.idEquips: {
                j = new Intent(getApplicationContext(), WebActivity.class);
                j.putExtra("url", "https://www.inc.com/aj-agrawal/6-ways-young-entrepreneurs-can-finance-business-ideas.html");
                startActivity(j);
            }
            break;
            case R.id.idReport:
                j = new Intent(getApplicationContext(), PieChartActivity.class);
                startActivity(j);
                break;
            case R.id.idLoans: {
                j = new Intent(getApplicationContext(), WebActivity.class);
                j.putExtra("url", "https://www.cashsuvidha.com/");
                startActivity(j);
            }
            break;
            case R.id.idSchemes: {
                j = new Intent(getApplicationContext(), WebActivity.class);
                j.putExtra("url", "http://www.skilldevelopment.gov.in/proposed-scheme.html");
                startActivity(j);
            }
            break;
            case R.id.idComplaint:
                j = new Intent(getApplicationContext(),feedback.class);
                j.putExtra("url", "http://www.skilldevelopment.gov.in/proposed-scheme.html");
                startActivity(j);
                break;
            case R.id.idstorage:
                j = new Intent(getApplicationContext(), WebActivity.class);
                j.putExtra("url", "https://dir.indiamart.com/impcat/cold-storage-services.html");
                startActivity(j);
                break;
        }
    }




}
