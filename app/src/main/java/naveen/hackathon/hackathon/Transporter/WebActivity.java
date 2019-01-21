package naveen.hackathon.hackathon.Transporter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import naveen.hackathon.hackathon.R;

public class WebActivity extends AppCompatActivity {

    WebView jcbWebpage;
    String url;
    ProgressDialog progressDialog;
    String category_name = null;
    int cate_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        jcbWebpage = (WebView) findViewById(R.id.idChevyWebPage);

        Bundle bundleWeb = getIntent().getExtras();

        // --- CHECK NETWORK STATE
        NetworkClass networkClass = new NetworkClass(WebActivity.this);
        boolean network_state = networkClass.checkInternet();
        if(network_state == true)
        {
            try {
                //url = getIntent().getExtras().getString("url").toString();
                url = bundleWeb.getString("url").toString();

            }catch (NullPointerException npe)
            {
                Log.e("TAG", "Error" + npe);
            }
            WebSettings webSettings = jcbWebpage.getSettings();
            webSettings.setJavaScriptEnabled(true);

            jcbWebpage.getSettings().setBuiltInZoomControls(true);
            jcbWebpage.getSettings().setUseWideViewPort(true);
            jcbWebpage.getSettings().setLoadWithOverviewMode(true);

            progressDialog = new ProgressDialog(WebActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            jcbWebpage.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                }
            });
            jcbWebpage.loadUrl(url);
        }
        else if(network_state == false) {
            new AlertDialog.Builder(WebActivity.this)
                    .setTitle("Internet Connection")
                    .setMessage("Oops! Check your internet")
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Navigator.getClassInstance().navigateToActivity(WebActivity.this, WebActivity.class);
                            startActivity(new Intent(WebActivity.this, WebActivity.class));

                        }
                    })
                    .setPositiveButton("GoTO Home", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Navigator.getClassInstance().navigateToActivity(WebActivity.this, HomePageActivity.class);
                            startActivity(new Intent(WebActivity.this, Transporter_home.class));
                        }
                    })
                    .show();
        }
    }

}
