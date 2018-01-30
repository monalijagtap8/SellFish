package com.example.android.sellfish;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewCart extends AppCompatActivity {
    VolleyRequest volleyRequest;
    String user_id;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    List<DataViewCart> data;
    JSONArray jArray;
    JSONObject json_data;
    RecyclerView recyclerView;
    AdapterViewCart adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        editor = sp.edit();
        user_id = sp.getString("USER_ID", "");
        volleyRequest = VolleyRequest.getObject();
        volleyRequest.setContext(getApplicationContext());
        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/fetchCartItems.rfa.php?user_id=" + user_id);
        volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/fetchCartItems.rfa.php?user_id=" + user_id);
        volleyRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) {

                Log.d("ResponseVC", response);
                if (!response.contains("nodata")) {
                    try {
                        data = new ArrayList<>();
                        jArray = new JSONArray(response);

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
                            Log.d(i + "", "loop");
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ViewCart.this, HomeActivity.class);
        finish();
        startActivity(intent);
    }
}
