package naveen.hackathon.hackathon.Transporter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import naveen.hackathon.hackathon.About;
import naveen.hackathon.hackathon.R;
import naveen.hackathon.hackathon.buyer_transport_price;
import naveen.hackathon.hackathon.category;
import naveen.hackathon.hackathon.change_pass;
import naveen.hackathon.hackathon.entrepreneur.entrepreneur_home;
import naveen.hackathon.hackathon.entrepreneur.weather;
import naveen.hackathon.hackathon.feedback;
import naveen.hackathon.hackathon.language;
import naveen.hackathon.hackathon.reports_charts.charts.linechart;

public class Transporter_home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private Intent j;
    private TextView verified_Textviewer,email_Textviewer;
    private String email,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transporter_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth=FirebaseAuth.getInstance();
        email=mAuth.getCurrentUser().getEmail().toString();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_transporter_home);
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
           // super.onBackPressed();
            moveTaskToBack (true);
        }
    }

    public void transporter(View view) {
        switch (view.getId())
        {
            case R.id.idInsurance:
                j = new Intent(getApplicationContext(), WebActivity.class);
                j.putExtra("url", "http://www.easyinsuranceindia.com/commercial-vehicle-goods-carrying.do");
                startActivity(j);
                break;
            case R.id.idInfo:
                j = new Intent(getApplicationContext(), WebActivity.class);
                j.putExtra("url", "http://web.worldbank.org/archive/website01291/WEB/0__CO-21.HTM");
                startActivity(j);
                break;
            case R.id.idBank:
                j = new Intent(getApplicationContext(), WebActivity.class);
                j.putExtra("url", "http://www.bankbazaar.com");
                startActivity(j);
                break;
            case R.id.idReports:
                j = new Intent(getApplicationContext(), linechart.class);
                startActivity(j);
                break;
            case R.id.idSea:
                j = new Intent(getApplicationContext(), WebActivity.class);
                j.putExtra("url", "http://www.bankersdaily/list-of-important-seaports-in-india/");
                startActivity(j);
                break;
            case R.id.idAir:
                j = new Intent(getApplicationContext(), WebActivity.class);
                j.putExtra("url", "http://www.cybex.in/Indian-Ports-Data.aspx");
                startActivity(j);
                break;
            case R.id.idComplaint:
                j = new Intent(getApplicationContext(), camera.class);
                startActivity(j);
                break;
            case R.id.idEvents:
                j = new Intent(getApplicationContext(), WebActivity.class);
                j.putExtra("url", "http://www.exhibitionsindia.com/events.html");
                startActivity(j);
                break;
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_Home) {
            startActivity(new Intent(getApplicationContext(),Transporter_home.class));

            finish();
        }
        else if (id == R.id.Profile) {
            startActivity(new Intent(getApplicationContext(),Transporter_reg.class));
        }  else if (id == R.id.vehicle_reg) {
            startActivity(new Intent(getApplicationContext(),VehicleRegistration.class));
        }  else if (id == R.id.change_pass) {
            startActivity(new Intent(getApplicationContext(),change_pass.class));
        }  else if (id == R.id.price) {
            startActivity(new Intent(getApplicationContext(),buyer_transport_price.class));
        }else if(id==R.id.update_staus_transpoter){
          startActivity(new Intent(Transporter_home.this, Transporter_update_status_Activity.class));
        } else if (id == R.id.feedback) {
            startActivity(new Intent(getApplicationContext(), feedback.class));
        } else if (id == R.id.weather) {
            startActivity(new Intent(getApplicationContext(), weather.class));
        }else if (id == R.id.About_us) {
            startNextMatchingActivity(new Intent(getApplicationContext(), About.class));
        }  else if (id == R.id.change_lan) {
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
}















