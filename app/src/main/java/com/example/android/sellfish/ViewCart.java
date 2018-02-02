package com.example.android.sellfish;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ViewCart extends AppCompatActivity {
    VolleyRequest volleyRequest;
    String user_id;
    @InjectView(R.id.btn_additem)
    Button addItem;
    @InjectView(R.id.btn_continue)
    Button btnContinue;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    List<DataViewCart> data;
    JSONArray jArray;
    JSONObject json_data;
    RecyclerView recyclerView;
    AdapterViewCart adapter;
    JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        ButterKnife.inject(this);
        sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        editor = sp.edit();
        user_id = sp.getString("USER_ID", "");
        volleyRequest = VolleyRequest.getObject();
        volleyRequest.setContext(getApplicationContext());
        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/fetchCartItems.rfa.php?user_id=17");
        volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/fetchCartItems.rfa.php?user_id=17");
        volleyRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d("ResponseVC", response);
                if (!response.contains("nodata")) {
                    try {
                        data = new ArrayList<>();
                        jArray = new JSONArray(response);
                        jsonArray = new JSONArray();
                        for (int i = 0; i < jArray.length(); i++) {
                            Log.d("JarrayLength", jArray.length() + "");
                            json_data = jArray.getJSONObject(i);
                            DataViewCart item_data = new DataViewCart();
                            item_data.name = json_data.getString("name");
                            item_data.type = json_data.getString("type");
                            item_data.desc = json_data.getString("description");
                            item_data.price = json_data.getInt("price");
                            item_data.id = json_data.getInt("id");
                            item_data.quantity = json_data.getInt("quantity");
                            item_data.image = json_data.getString("image_path");
                            item_data.user_id = json_data.getString("userId");
                            item_data.item_id = json_data.getString("itemId");
                            data.add(item_data);
                            JSONObject object = new JSONObject();
                            object.put("item_id", item_data.item_id);
                            object.put("user_id", item_data.user_id);
                            jsonArray.put(object.toString());

                            Log.d(jsonArray + "", "Array");
                            Log.d(data.size() + "", "data");
                            Log.d(item_data.name + "", "dataname");
                        }
                        recyclerView = findViewById(R.id.cartlist);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ViewCart.this));
                        adapter = new AdapterViewCart(ViewCart.this, data);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }
        });
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewCart.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(ViewCart.this);
                final View enquireDialog = li.inflate(R.layout.dialouge_payment, null);
                final TextView txtName = enquireDialog.findViewById(R.id.txtName);
                final RadioGroup radiogrp = enquireDialog.findViewById(R.id.radioBtnGroup);
                final RadioButton radiobtnCashOnDelivery = enquireDialog.findViewById(R.id.radioBtnCashOnDelivery);
                final RadioButton radiobtnPayUMoney = enquireDialog.findViewById(R.id.radioBtnPayUMoney);

                final Button buttonContinue = enquireDialog.findViewById(R.id.buttonCountinue);

                AlertDialog.Builder alert = new AlertDialog.Builder(ViewCart.this);
                //Adding our dialog box to the view of alert dialog
                alert.setView(enquireDialog);

                //Creating an alert dialog
                final AlertDialog alertDialog = alert.create();
                radiobtnCashOnDelivery.setText("Cash On Delivery");
                radiobtnPayUMoney.setText("PayUMoney");
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("jsonObject", jsonArray);
                    Log.d(jsonObject + "", "object");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                buttonContinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String number;
                        if (radiobtnCashOnDelivery.isChecked()) {
                            Intent intent = new Intent(ViewCart.this, CashOnDelivery.class);
                            intent.putExtra("OBJECT", jsonObject + "");
                            startActivity(intent);
                            alertDialog.dismiss();
                            finish();

                        } else
                            Toast.makeText(ViewCart.this, "PayUMoney will be add soon", Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ViewCart.this, HomeActivity.class);
        finish();
        startActivity(intent);
    }
}
