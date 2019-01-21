
package naveen.hackathon.hackathon.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import naveen.hackathon.hackathon.About;
import naveen.hackathon.hackathon.Buyer_reg;
import naveen.hackathon.hackathon.R;
import naveen.hackathon.hackathon.buyer_transport_price;
import naveen.hackathon.hackathon.category;
import naveen.hackathon.hackathon.change_pass;
import naveen.hackathon.hackathon.domain.helper.Connectivity;
import naveen.hackathon.hackathon.cancel_order;
import naveen.hackathon.hackathon.feedback;
import naveen.hackathon.hackathon.language;
import naveen.hackathon.hackathon.location;
import naveen.hackathon.hackathon.model.CenterRepository;
import naveen.hackathon.hackathon.model.entities.Money;
import naveen.hackathon.hackathon.model.entities.Product;
import naveen.hackathon.hackathon.payment_gateway;
import naveen.hackathon.hackathon.util.PreferenceHelper;
import naveen.hackathon.hackathon.util.TinyDB;
import naveen.hackathon.hackathon.util.Utils;
import naveen.hackathon.hackathon.util.Utils.AnimationType;
import naveen.hackathon.hackathon.fragment.HomeFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.wang.avi.AVLoadingIndicatorView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

public class ECartHomeActivity extends AppCompatActivity {

    public static final double MINIMUM_SUPPORT = 0.1;
    private static final String TAG = ECartHomeActivity.class.getSimpleName();
    private int itemCount = 0;
    private BigDecimal checkoutAmount = new BigDecimal(BigInteger.ZERO);
    private DrawerLayout mDrawerLayout;
    public static ArrayList<String> productId;
    public static ArrayList<String> productdiscount;
    public static ArrayList<String> productprice;
    public static ArrayList<String> productquantity;

    private TextView checkOutAmount, itemCountTextView;
    private TextView offerBanner;
    private AVLoadingIndicatorView progressBar;

    private NavigationView mNavigationView;
    private FirebaseAuth mAuth;
    private String email,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecart);
        mAuth=FirebaseAuth.getInstance();

        CenterRepository.getCenterRepository().setListOfProductsInShoppingList(
                new TinyDB(getApplicationContext()).getListObject(
                        PreferenceHelper.MY_CART_LIST_LOCAL, Product.class));

        itemCount = CenterRepository.getCenterRepository().getListOfProductsInShoppingList()
                .size();

        //	makeFakeVolleyJsonArrayRequest();

        offerBanner = ((TextView) findViewById(R.id.new_offers_banner));

        itemCountTextView = (TextView) findViewById(R.id.item_count);
        itemCountTextView.setSelected(true);
        itemCountTextView.setText(String.valueOf(itemCount));

        checkOutAmount = (TextView) findViewById(R.id.checkout_amount);
        checkOutAmount.setSelected(true);
        checkOutAmount.setText(Money.rupees(checkoutAmount).toString());
        offerBanner.setSelected(true);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        progressBar = (AVLoadingIndicatorView) findViewById(R.id.loading_bar);

        checkOutAmount.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Utils.vibrate(getApplicationContext());

                Utils.switchContent(R.id.frag_container,
                        Utils.SHOPPING_LIST_TAG, ECartHomeActivity.this,
                        AnimationType.SLIDE_UP);

            }
        });


        if (itemCount != 0) {
            for (Product product : CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList()) {

                updateCheckOutAmount(
                        BigDecimal.valueOf(Long.valueOf(product.getSellMRP())),
                        true);
            }
        }

        findViewById(R.id.item_counter).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Utils.vibrate(getApplicationContext());
                        Utils.switchContent(R.id.frag_container,
                                Utils.SHOPPING_LIST_TAG,
                                ECartHomeActivity.this, AnimationType.SLIDE_UP);

                    }
                });

        findViewById(R.id.checkout).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Utils.vibrate(getApplicationContext());

                        showPurchaseDialog();

                    }
                });

        Utils.switchFragmentWithAnimation(R.id.frag_container,
                new HomeFragment(), this, Utils.HOME_FRAGMENT,
                AnimationType.SLIDE_UP);

        toggleBannerVisibility();

        mNavigationView
                .setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        // Handle navigation view item clicks here.
                        menuItem.setChecked(true);
                        int id = menuItem.getItemId();

                        if(id == R.id.nav_Home) {
                            startActivity(new Intent(ECartHomeActivity.this,ECartHomeActivity.class));
                            finish();
                        }
                        else if (id == R.id.Profile) {
                            startActivity(new Intent(getApplicationContext(),Buyer_reg.class));

                        }
                        else if (id == R.id.change_pass) {
                            mDrawerLayout.closeDrawers();
                            startActivity(new Intent(getApplicationContext(),change_pass.class));
                        }

                        else if (id == R.id.my_cart) {
                            mDrawerLayout.closeDrawers();

                            Utils.switchContent(R.id.frag_container,
                                    Utils.SHOPPING_LIST_TAG,
                                    ECartHomeActivity.this,
                                    AnimationType.SLIDE_LEFT);
                        }
                            else if (id == R.id.price) {
                            mDrawerLayout.closeDrawers();
                            startActivity(new Intent(getApplicationContext(), buyer_transport_price.class));

                        }   else if (id == R.id.cancel) {
                            final ProgressDialog dialog = ProgressDialog.show(ECartHomeActivity.this, "", "Deleting",
                                    true);
                            dialog.show();
                            new CountDownTimer(5000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    // You don't need anything here
                                }

                                public void onFinish() {
                                    dialog.dismiss();
                                    startActivity(new Intent(getApplicationContext(), cancel_order.class));
                                }
                            }.start();
                        }   else if (id == R.id.About_us) {
                            startActivity(new Intent(getApplicationContext(), About.class));
                        }   else if (id == R.id.feedback) {
                            mDrawerLayout.closeDrawers();
                            startActivity(new Intent(getApplicationContext(), feedback.class));
                        }   else if (id == R.id.change_lan) {
                            mDrawerLayout.closeDrawers();
                            Intent intent=new Intent(getApplicationContext(),language.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("key", "Buyer_class");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }  else if (id == R.id.Log_out) {
                            // Firebase sign out
                            mAuth.signOut();
                            startActivity(new Intent(getApplicationContext(), category.class));
                            finish();
                        }

                        return false;
                    }
                });

    }

    public AVLoadingIndicatorView getProgressBar() {
        return progressBar;
    }



    public void updateItemCount(boolean ifIncrement) {
        if (ifIncrement) {
            itemCount++;
            itemCountTextView.setText(String.valueOf(itemCount));

        } else {
            itemCountTextView.setText(String.valueOf(itemCount <= 0 ? 0
                    : --itemCount));
        }

        toggleBannerVisibility();
    }

    public void updateCheckOutAmount(BigDecimal amount, boolean increment) {

        if (increment) {
            checkoutAmount = checkoutAmount.add(amount);
        } else {
            if (checkoutAmount.signum() == 1)
                checkoutAmount = checkoutAmount.subtract(amount);
        }

        checkOutAmount.setText(Money.rupees(checkoutAmount).toString());
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Store Shopping Cart in DB
        new TinyDB(getApplicationContext()).putListObject(
                PreferenceHelper.MY_CART_LIST_LOCAL, CenterRepository
                        .getCenterRepository().getListOfProductsInShoppingList());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Show Offline Error Message
        if (!Connectivity.isConnected(getApplicationContext())) {
            final Dialog dialog = new Dialog(ECartHomeActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.connection_dialog);
            Button dialogButton = (Button) dialog
                    .findViewById(R.id.dialogButtonOK);

            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });

            dialog.show();
        }

        // Show Whats New Features If Requires
    }

    public void toggleBannerVisibility() {
        if (itemCount == 0) {

            findViewById(R.id.checkout_item_root).setVisibility(View.GONE);
            findViewById(R.id.new_offers_banner).setVisibility(View.VISIBLE);

        } else {
            findViewById(R.id.checkout_item_root).setVisibility(View.VISIBLE);
            findViewById(R.id.new_offers_banner).setVisibility(View.GONE);
        }
    }

    public BigDecimal getCheckoutAmount() {
        return checkoutAmount;
    }
    public int getItemCount() {
        return itemCount;
    }

    public DrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }


    public void showPurchaseDialog() {

        AlertDialog.Builder exitScreenDialog = new AlertDialog.Builder(ECartHomeActivity.this, R.style.PauseDialog);

        exitScreenDialog.setTitle("Order Confirmation")
                .setMessage("Would you like to place this order ?");
        exitScreenDialog.setCancelable(true);

        exitScreenDialog.setPositiveButton(
                "Place Order",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //finish();
                        dialog.cancel();

                        productId = new ArrayList<String>();
                        productdiscount = new ArrayList<String>();
                        productprice = new ArrayList<String>();
                        productquantity = new  ArrayList<String>();

                        for (Product productFromShoppingList : CenterRepository.getCenterRepository().getListOfProductsInShoppingList()) {

                            //add product ids to array
                            productId.add(productFromShoppingList.getProductId());
                            productdiscount.add(productFromShoppingList.getDiscount());
                            productprice.add(productFromShoppingList.getSellMRP());
                            productquantity.add(productFromShoppingList.getQuantity());
                        }
                        Log.e("Product Buyed", productId.get(0));
                        startActivity(new Intent(getApplicationContext(),location.class));


                        CenterRepository.getCenterRepository()
                                .addToItemSetList(new HashSet<>(productId));

                        //clear all list item
                        CenterRepository.getCenterRepository().getListOfProductsInShoppingList().clear();

                        toggleBannerVisibility();

                        itemCount = 0;
                        itemCountTextView.setText(String.valueOf(0));
                        checkoutAmount = new BigDecimal(BigInteger.ZERO);
                        checkOutAmount.setText(Money.rupees(checkoutAmount).toString());

                    }
                });

        exitScreenDialog.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = exitScreenDialog.create();
        alert11.show();

    }

}
