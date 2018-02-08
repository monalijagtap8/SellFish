package com.example.android.sellfish;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static List<ItemSuggestions> items;
    @InjectView(R.id.search_view)
    FloatingSearchView searchView;
    @InjectView(R.id.img_viewCart)
    ImageView viewCart;
    @InjectView(R.id.cartCount)
    TextView cartCount;
    @InjectView(R.id.slider)
    SliderLayout sliderShow;
    AdapterCart adapter;
    List<DataCart> data;
    Animation animation;
    RecyclerView recyclerView;
    VolleyRequest volleyRequest;
    JSONArray jArray;
    JSONObject json_data;
    String user_id;
    SharedPreferences sp;
    long back_pressed = 0;
    Toast toast;
    SharedPreferences.Editor editor;
    Intent intent;
    int imageCount = 2131165299;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        editor = sp.edit();
        user_id = sp.getString("USER_ID", "");
        for (int i = 1; i <= 4; i++) {
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            textSliderView.image(imageCount);
            imageCount++;
            sliderShow.addSlider(textSliderView);
            animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
            Log.d(R.drawable.f1 + " " + R.drawable.f2 + " " + R.drawable.f3 + " " + R.drawable.f3, "f");

        }

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        getItemCount();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        volleyRequest=VolleyRequest.getObject();
        volleyRequest.setContext(getApplicationContext());
        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/fetchItems.rfa.php");
        volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/fetchItems.rfa.php");
        volleyRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) {

                Log.d("ResponseHome", response);
                if (!response.contains("nodata")) {
                    try {
                        data = new ArrayList<>();
                        jArray = new JSONArray(response);

                        for (int i = 0; i < jArray.length(); i++) {
                            Log.d("JarrayLength", jArray.length() + "");
                            json_data = jArray.getJSONObject(i);
                            DataCart item_data = new DataCart();
                            item_data.name = json_data.getString("itemName");
                            item_data.type = json_data.getString("type");
                            item_data.desc = json_data.getString("description");
                            item_data.price = json_data.getString("price");
                            item_data.id = json_data.getInt("id");
                            item_data.image=json_data.getString("image_path");
                            data.add(item_data);
                            Log.d(i+"", "loop");
                            Log.d(data.size()+"", "data");
                            Log.d(item_data.name+"", "dataname");
                        }
                        recyclerView = findViewById(R.id.Listmenu);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));
                        recyclerView.setNestedScrollingEnabled(false);
                        recyclerView.setHasFixedSize(false);
                        adapter = new AdapterCart(HomeActivity.this, data);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {

                        e.printStackTrace();

                    }
                }
            }
        });
        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ViewCart.class);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_pressed + 2000 > System.currentTimeMillis()) {
                // need to cancel the toast here
                toast.cancel();
                // code for exit
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else {
                // ask user to press back button one more time to close app
                toast = Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT);
                toast.show();
            }
            back_pressed = System.currentTimeMillis();
        }
    }
    public void getItemCount() {
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
                cartCount.setText(response);
                if (count > 9) {
                    cartCount.setPadding(8, 0, 0, 0);
                } else {
                    cartCount.setPadding(14, 0, 0, 0);
                }


            }
        });
    }

    /* @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.home, menu);
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {

         int id = item.getItemId();

         if (id == R.id.action_settings) {
             return true;
         }

         return super.onOptionsItemSelected(item);
     }
 */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user_account) {
            intent = new Intent(HomeActivity.this, UserProfile.class);
            finish();
            startActivity(intent);

        } else if (id == R.id.nav_home) {
            intent = new Intent(HomeActivity.this, HomeActivity.class);
            finish();
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
