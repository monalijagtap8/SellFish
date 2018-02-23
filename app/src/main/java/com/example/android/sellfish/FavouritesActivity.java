package com.example.android.sellfish;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.view.View.VISIBLE;

public class FavouritesActivity extends AppCompatActivity {

    VolleyRequest volleyRequest;
    SharedPreferences sp;
    String user_id;
    JSONArray jArray;
    JSONObject json_data;
    ArrayList<DataCart> data;
    RecyclerView recyclerView;
    AdapterFavourites adapter;
    @InjectView(R.id.img_back)
    ImageView imgBack;
    @InjectView(R.id.img_viewCart)
    ImageView imgCart;
    @InjectView(R.id.cartCount1)
    TextView txtCount;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        ButterKnife.inject(this);
        sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        user_id = sp.getString("USER_ID", "");

        getItemCount1();
        checkFavourie();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(FavouritesActivity.this, ViewCart.class);
                finish();
                startActivity(intent);
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

    public void checkFavourie() {
        volleyRequest = VolleyRequest.getObject();
        volleyRequest.setContext(FavouritesActivity.this);
        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/favourites.rfa.php?user_id=" + user_id);
        volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/favourites.rfa.php?user_id=" + user_id);
        volleyRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    data = new ArrayList<>();
                    jArray = new JSONArray(response);
                    for (int i = 0; i < jArray.length(); i++) {
                        Log.d("checkF", response);
                        json_data = jArray.getJSONObject(i);
                        DataCart data_item = new DataCart();
                        json_data = jArray.getJSONObject(i);
                        data_item.name = json_data.getString("itemName");
                        data_item.desc = json_data.getString("description");
                        data_item.price = json_data.getString("price");
                        data_item.id = json_data.getInt("id");
                        data_item.image = json_data.getString("image_path");
                        data.add(data_item);
                    }
                    recyclerView = findViewById(R.id.listview_subcategory);
                    recyclerView.setVisibility(VISIBLE);
                    recyclerView.setLayoutManager(new GridLayoutManager(FavouritesActivity.this, 1));
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setHasFixedSize(false);
                    adapter = new AdapterFavourites(FavouritesActivity.this, data);
                    // Log.d(data + "", "data*************");
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent(FavouritesActivity.this, HomeActivity.class);
        finish();
        startActivity(intent);
    }
}
