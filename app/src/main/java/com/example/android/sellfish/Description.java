package com.example.android.sellfish;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Description extends AppCompatActivity {
    String name, image, price, available, desc, item_id, user_id;
    @InjectView(R.id.txt_name)
    TextView txt_name;
    @InjectView(R.id.txt_price)
    TextView txt_price;
    @InjectView(R.id.txt_availability)
    TextView txt_available;
    @InjectView(R.id.txt_description)
    TextView txt_description;
    @InjectView(R.id.img_view)
    ImageView imgView;
    @InjectView(R.id.btn_addToCart)
    Button btnAddToCart;
    @InjectView(R.id.btn_buyNow)
    Button btnBuyNow;
    @InjectView(R.id.img_back)
    ImageView imgBack;
    @InjectView(R.id.cartCount1)
    TextView txtCount;
    @InjectView(R.id.img_viewCart)
    ImageView imgCart;
    @InjectView(R.id.toggleFavourite)
    ToggleButton toggleFavourite;
    VolleyRequest volleyRequest;
    TextView tv;
    EditText et;
    JSONObject object;
    Intent intent;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        ButterKnife.inject(this);
        sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        user_id = sp.getString("USER_ID", "");

        getItemCount1();

        name = getIntent().getStringExtra("NAME");
        image = getIntent().getStringExtra("IMAGE");
        price = getIntent().getStringExtra("PRICE");
        item_id = getIntent().getStringExtra("ITEM_ID");
        user_id = getIntent().getStringExtra("USER_ID");
        available = getIntent().getStringExtra("AVAILABILITY");
        desc = getIntent().getStringExtra("DESCRIPTION");
        Log.d(name, "name");
        Log.d(user_id, "user");
        txt_name.setText(name);
        txt_price.setText(price);
        txt_available.setText(available);
        txt_description.setText(desc);
        checkFavourie();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("item_id", item_id);
            jsonObject.put("user_id", user_id);
            JSONArray jArray = new JSONArray();
            jArray.put(jsonObject);
            object = new JSONObject();
            object.put("jsonObject", jArray);
            Log.d("object", object + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Glide.with(Description.this).load("http://192.168.0.110:8001/routes/server/" + image).asBitmap().override(600, 600)
                .placeholder(null).listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                Log.d("image", "http://192.168.0.110:8001/routes/server/" + image);
                imgView.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                // myHolder.imageView.setVisibility(View.GONE);
                Log.d("image", "http://192.168.0.110:8001/routes/server/" + image);
                return false;
            }
        }).error(null).into(imgView);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toggleFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavourite();
            }
        });

        imgCart.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Description.this, ViewCart.class);
                finish();
                startActivity(intent);
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (sp.getBoolean("LOGGED_IN", false)) {
                    volleyRequest = VolleyRequest.getObject();
                    volleyRequest.setContext(Description.this);
                    Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/addToCart.rfa.php?user_id=" + user_id + "&item_id=" + item_id);
                    volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/addToCart.rfa.php?user_id=" + user_id + "&item_id=" + item_id);
                    volleyRequest.getResponse(new ServerCallback() {
                        @Override
                        public void onSuccess(String response) {

                            Log.d("cbcjhe", response);
                            getItemCount1();
                            Toast.makeText(Description.this, "Item added to cart", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    intent = new Intent(getApplicationContext(), TabActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(Description.this);
                final View enquireDialog = li.inflate(R.layout.dialouge_payment, null);
                final TextView txtName = enquireDialog.findViewById(R.id.txtName);
                final RadioGroup radiogrp = enquireDialog.findViewById(R.id.radioBtnGroup);
                final RadioButton radiobtnCashOnDelivery = enquireDialog.findViewById(R.id.radioBtnCashOnDelivery);
                final RadioButton radiobtnPayUMoney = enquireDialog.findViewById(R.id.radioBtnPayUMoney);

                final Button btnContinue = enquireDialog.findViewById(R.id.buttonCountinue);

                AlertDialog.Builder alert = new AlertDialog.Builder(Description.this);
                //Adding our dialog box to the view of alert dialog
                alert.setView(enquireDialog);

                //Creating an alert dialog
                final AlertDialog alertDialog = alert.create();
                radiobtnCashOnDelivery.setText("Cash On Delivery");
                radiobtnPayUMoney.setText("PayUMoney");

                btnContinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String number;
                        if (radiobtnCashOnDelivery.isChecked()) {
                            Intent intent = new Intent(Description.this, CashOnDelivery.class);
                            intent.putExtra("OBJECT", object + "");
                            startActivity(intent);
                            alertDialog.dismiss();
                            finish();

                        } else
                            Toast.makeText(Description.this, "PayUMoney will be add soon", Toast.LENGTH_LONG).show();
                    }
                });
                if (sp.getBoolean("LOGGED_IN", false)) {
                    alertDialog.show();
                } else {
                    intent = new Intent(getApplicationContext(), TabActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    public void getItemCount1() {
        volleyRequest = VolleyRequest.getObject();
        volleyRequest.setContext(getApplicationContext());
        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/totalCartItems.rfa.php?user_id=" + user_id);
        volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/totalCartItems.rfa.php?user_id=" + user_id);
        volleyRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(response, "count");
                int count = Integer.parseInt(response);
                Log.d(response, "rcount");
                txtCount.setText(response);
                if (count > 9) {
                    txtCount.setPadding(4, 0, 0, 0);
                } else {
                    txtCount.setPadding(14, 0, 0, 0);
                }
            }
        });
    }

    public void setFavourite() {
        if (sp.getBoolean("LOGGED_IN", false)) {
            volleyRequest = VolleyRequest.getObject();
            volleyRequest.setContext(Description.this);
            Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/addToFavourites.rfa.php?user_id=" + user_id + "&item_id=" + item_id);
            volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/addToFavourites.rfa.php?user_id=" + user_id + "&item_id=" + item_id);
            volleyRequest.getResponse(new ServerCallback() {
                @Override
                public void onSuccess(String response) {

                    Log.d("cbcjhe", response);
                    if (response.contains("ADDED")) {
                        toggleFavourite.setChecked(true);
                        Toast.makeText(Description.this, "Item added to favourite", Toast.LENGTH_LONG).show();
                    } else {
                        toggleFavourite.setChecked(false);
                        Toast.makeText(Description.this, "Item removed from favourite", Toast.LENGTH_LONG).show();
                    }

                }
            });
        } else {
            intent = new Intent(getApplicationContext(), TabActivity.class);
            finish();
            startActivity(intent);
        }
    }

    public void checkFavourie() {
        volleyRequest = VolleyRequest.getObject();
        volleyRequest.setContext(Description.this);
        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/favourites.rfa.php?user_id=" + user_id + "&item_id=" + item_id);
        volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/favourites.rfa.php?user_id=" + user_id + "&item_id=" + item_id);
        volleyRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) {

                Log.d("checkF", response);
                if (response.contains("EXISTS")) {
                    toggleFavourite.setChecked(true);
                } else {
                    toggleFavourite.setChecked(false);
                }
            }
        });
    }
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.desc_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        intent=new Intent(Description.this, SubCategoryActivity.class);
        intent.putExtra("Activity","Fish");
        startActivity(intent);
        finish();
    }
}


