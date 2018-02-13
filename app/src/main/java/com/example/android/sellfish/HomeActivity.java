package com.example.android.sellfish;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
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

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    public static List<ItemSuggestions> items;
    @InjectView(R.id.img_viewCart)
    ImageView viewCart;
    /* @InjectView(R.id.floating_search_view)
     FloatingSearchView searchView;*/
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
    String user_id, item_name, name, desc, image, price;
    int item_id;
    SharedPreferences sp;
    long back_pressed = 0;
    Toast toast;
    SharedPreferences.Editor editor;
    Intent intent;
    FloatingSearchView searchView;
    int imageCount = 2131165299;
    private String mLastQuery = "Search...", TAG;

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
       /* searchView.attachNavigationDrawerToMenuButton(drawer);
        searchView.setDismissOnOutsideClick(true);
        searchView.setDismissOnOutsideClick(true);*/
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
        items = new ArrayList<>();
        TAG = "*****kay rao**";
        fetchName();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (FloatingSearchView) MenuItemCompat.getActionView(searchItem);
        searchView.swapSuggestions(items);
        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                searchView.showProgress();
                List<ItemSuggestions> filteredcities = new ArrayList<>();

                for (ItemSuggestions i : items) {
                    Log.d("**********", String.valueOf(i.getBody().contains(newQuery)) + "******" + newQuery);
                    if (i.getBody().toLowerCase().contains(newQuery.toLowerCase())) {
                        filteredcities.add(i);
                        Log.d("**********", i.getBody());
                    }

                }
                searchView.swapSuggestions(filteredcities);

                searchView.hideProgress();
            }


        });

        searchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, SearchSuggestion item, int itemPosition) {
                ItemSuggestions citySuggetions = (ItemSuggestions) item;

                if (citySuggetions.getBody().equalsIgnoreCase(mLastQuery))
                    citySuggetions.setHistory(true);
                Log.d("zandan", "am called" + itemPosition + "  " + item.getBody());
                if (citySuggetions.isHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));

                    Util.setIconColor(leftIcon, Color.GRAY);
                    leftIcon.setAlpha(1f);
                } else {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_call_received_black_24dp, null));

                    Util.setIconColor(leftIcon, Color.GRAY);
                    leftIcon.setAlpha(.30f);
                }
            }
        });
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                ItemSuggestions citySuggetions1 = (ItemSuggestions) searchSuggestion;


                Log.d("search", jArray.toString());

                mLastQuery = searchSuggestion.getBody();
                /////////////////Broooooooooooo here we can launch new activity///////////////////////////////
                try {


                    for (int i = 0; i < jArray.length(); i++) {
                        Log.d("JarrayLength", jArray.length() + "");
                        json_data = jArray.getJSONObject(i);

                        name = json_data.getString("itemName");
                        desc = json_data.getString("description");
                        price = json_data.getString("price");
                        item_id = json_data.getInt("id");
                        image = json_data.getString("image_path");

                        items.add(new ItemSuggestions(item_name));
                    }
                    searchView.swapSuggestions(items);

                    Log.d("addedList", items.get(0) + "");
                } catch (JSONException e) {

                    e.printStackTrace();

                }

                // Intent i = new Intent(HomeActivity.this, Description.class);
                Intent intent = new Intent(HomeActivity.this, Description.class);
                intent.putExtra("NAME", name);
                intent.putExtra("IMAGE", image);
                intent.putExtra("ITEM_ID", item_id + "");
                intent.putExtra("USER_ID", user_id + "");
                Log.d(user_id, "intent");
                intent.putExtra("PRICE", price + "");
                intent.putExtra("AVAILABILITY", "available");
                intent.putExtra("DESCRIPTION", desc);
                finish();
                startActivity(intent);

                Toast.makeText(HomeActivity.this, "hhhhhhhhh", Toast.LENGTH_LONG).show();
//Create the bundle
                Bundle bundle = new Bundle();

//Add your data to bundle
                bundle.putString("item", mLastQuery.trim());

//Add the bundle to the intent
                intent.putExtras(bundle);

                ActivityOptions options = ActivityOptions.makeScaleUpAnimation(searchView.getRootView(), 0, 0, 280, 280);
//Fire that second activity
                if (!mLastQuery.contains("No result found")) {
                    startActivity(intent, options.toBundle());
                    searchView.setSearchBarTitle(mLastQuery);
                    searchView.clearSearchFocus();
                }

            }

            @Override
            public void onSearchAction(String currentQuery) {
                List<ItemSuggestions> filteredcities = new ArrayList<>();
                for (ItemSuggestions i : items) {
                    if (i.getBody().contains(currentQuery)) {
                        filteredcities.add(i);

                    }

                }
                Log.d("harshu", currentQuery);
                Log.d("harshada", "loves sourabh and viceversa");
                Log.d("filter", filteredcities + "");
                if (filteredcities.size() < 1)
                    filteredcities.add(new ItemSuggestions("No result found"));
                searchView.swapSuggestions(filteredcities);
            }
        });
//        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
//            @Override
//            public void onFocus() {
//
//                ObjectAnimator anim = ObjectAnimator.ofFloat(searchView, "translationY",
//                        300, 0);
//                anim.setDuration(350);
//
//                anim.addListener(new AnimatorListenerAdapter() {
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        //show suggestions when search bar gains focus (typically history suggestions)
//                        fetchName();
//                    }
//                });
//                anim.start();
//
//
//            }

//            @Override
//            public void onFocusCleared() {
//
//                //set the title of the bar so that when focus is returned a new query begins
//                searchView.setSearchBarTitle(mLastQuery);
//            }
//        });
        searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                Log.d("************", item.getTitle().toString());
                //// TODO: 21/8/17  can implement location menu item

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "Search for place or area");
                startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);


            }
        });
        searchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                //to sync recycler
            }
        });
        // searchView.setOnQueryTextListener(HomeActivity.this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    void fetchName() {
        volleyRequest = VolleyRequest.getObject();
        volleyRequest.setContext(getApplicationContext());
        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/fetchItems.rfa.php");
        volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/fetchItems.rfa.php");
        volleyRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) {

                Log.d("Responseitem", response);
                if (!response.contains("nodata")) {
                    try {

                        jArray = new JSONArray(response);

                        for (int i = 0; i < jArray.length(); i++) {
                            Log.d("JarrayLength", jArray.length() + "");
                            json_data = jArray.getJSONObject(i);

                            item_name = json_data.getString("itemName");
                            Log.d(item_name, "item");
                            items.add(new ItemSuggestions(item_name));
                        }
                        searchView.swapSuggestions(items);

                        Log.d("addedList", items.get(0) + "");
                    } catch (JSONException e) {

                        e.printStackTrace();

                    }
                }
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
                    cartCount.setPadding(4, 0, 0, 0);
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
