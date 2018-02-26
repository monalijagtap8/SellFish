package com.example.android.sellfish;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchActivity extends AppCompatActivity {

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    public static List<ItemSuggestions> items;
    DrawerLayout drawer;
    @InjectView(R.id.floating_search_view1)
    FloatingSearchView floatingSearchView;
    VolleyRequest volleyRequest;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    JSONArray jArray;
    JSONObject json_data;
    ArrayList<String> search_list;
    String item_name, name, desc, price, image, user_id;
    int item_id;
    private View mDifloatingSearchViewBackground;
    private ColorDrawable mDimDrawable;
    private String mLastQuery = "Search...", TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);
        items = new ArrayList<>();
        fetchName();
//        floatingSearchView.bringToFront();
        sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        editor = sp.edit();
        user_id = sp.getString("USER_ID", "");
        floatingSearchView.requestFocus();
        mDifloatingSearchViewBackground = findViewById(R.id.dim_background);
        mDimDrawable = new ColorDrawable(Color.BLACK);
        mDimDrawable.setAlpha(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mDifloatingSearchViewBackground.setBackground(mDimDrawable);
        } else {
            mDifloatingSearchViewBackground.setBackgroundDrawable(mDimDrawable);
        }
        floatingSearchView.swapSuggestions(items);
        floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                floatingSearchView.showProgress();
                List<ItemSuggestions> filtereditems = new ArrayList<>();

                for (ItemSuggestions i : items) {
                    Log.d("**********", String.valueOf(i.getBody().contains(newQuery)) + "******" + newQuery);
                    if (i.getBody().toLowerCase().contains(newQuery.toLowerCase())) {
                        filtereditems.add(i);
                        Log.d("**********", i.getBody());
                    }

                }
                floatingSearchView.swapSuggestions(filtereditems);

                floatingSearchView.hideProgress();
            }


        });

        floatingSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, SearchSuggestion item, int itemPosition) {
                ItemSuggestions ItemSuggestions = (ItemSuggestions) item;
//                findViewById(R.id.sliderlayout1).setVisibility(View.INVISIBLE);
                if (ItemSuggestions.getBody().equalsIgnoreCase(mLastQuery))
                    ItemSuggestions.setHistory(true);
                Log.d("zandan", "am called" + itemPosition + "  " + item.getBody());
                if (ItemSuggestions.isHistory()) {
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
        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                ItemSuggestions ItemSuggestions1 = (ItemSuggestions) searchSuggestion;


                mLastQuery = searchSuggestion.getBody();
                Log.d("searchHS", mLastQuery + "");
                /////////////////Broooooooooooo here we can launch new activity///////////////////////////////

                Intent i = new Intent(SearchActivity.this, HomeActivity.class);


//Create the bundle
                Bundle bundle = new Bundle();

//Add your data to bundle
                bundle.putString("city", mLastQuery.trim());

//Add the bundle to the intent
                i.putExtras(bundle);

                ActivityOptions options = ActivityOptions.makeScaleUpAnimation(floatingSearchView.getRootView(), 0, 0, 280, 280);
//Fire that second activity
                if (!mLastQuery.contains("No result found")) {
                    startActivity(i, options.toBundle());
                    floatingSearchView.setSearchBarTitle(mLastQuery);
                    floatingSearchView.clearSearchFocus();
                }

            }

            @Override
            public void onSearchAction(String currentQuery) {
                List<ItemSuggestions> filtereditems = new ArrayList<>();
                for (ItemSuggestions i : items) {
                    if (i.getBody().contains(currentQuery)) {
                        filtereditems.add(i);

                    }

                }

                if (filtereditems.size() < 1)
                    filtereditems.add(new ItemSuggestions("No result found"));
                floatingSearchView.swapSuggestions(filtereditems);

            }
        });
        floatingSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

                //                int headerHeight = findViewById(R.id.).getHeight();
                ObjectAnimator anim = ObjectAnimator.ofFloat(findViewById(R.id.jugaad), "translationY",
                        -500, 500);
                anim.setDuration(350);
//                fadeDimBackground(0, 150, null);
                anim.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //show suggestions when search bar gains focus (typically history suggestions)
                        fetchName();
                    }
                });
                anim.start();


            }

            @Override
            public void onFocusCleared() {

                ObjectAnimator anim = ObjectAnimator.ofFloat(floatingSearchView, "translationY",
                        0, 0);
                anim.setDuration(350);
                anim.start();
                //fadeDimBackground(150, 0, null);

                //set the title of the bar so that when focus is returned a new query begins
                floatingSearchView.setSearchBarTitle(mLastQuery);
            }
        });
        floatingSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
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
        floatingSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {

                //to sync recycler
            }
        });

        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                ItemSuggestions ItemSuggestions1 = (ItemSuggestions) searchSuggestion;


                mLastQuery = searchSuggestion.getBody();
                try {


                    for (int i = 0; i < jArray.length(); i++) {
                        Log.d("JarrayLength", jArray.length() + "");
                        json_data = jArray.getJSONObject(i);
                        name = json_data.getString("itemName");
                        desc = json_data.getString("description");
                        price = json_data.getString("price");
                        item_id = json_data.getInt("id");
                        image = json_data.getString("image_path");


                    }
                    floatingSearchView.swapSuggestions(items);

                    Log.d("addedList", items.get(0) + "");
                } catch (JSONException e) {

                    e.printStackTrace();

                }


                Intent intent = new Intent(SearchActivity.this, Description.class);
                intent.putExtra("NAME", name);
                intent.putExtra("IMAGE", image);
                intent.putExtra("ITEM_ID", item_id + "");
                intent.putExtra("USER_ID", user_id + "");
                Log.d(user_id, "intent");
                intent.putExtra("PRICE", price + "");
                intent.putExtra("AVAILABILITY", "available");
                intent.putExtra("DESCRIPTION", desc);

                startActivity(intent);
                finish();

//Create the bundle
                Bundle bundle = new Bundle();

//Add your data to bundle
                bundle.putString("city", mLastQuery.trim());

//Add the bundle to the intent
                intent.putExtras(bundle);

                ActivityOptions options = ActivityOptions.makeScaleUpAnimation(floatingSearchView.getRootView(), 0, 0, 280, 280);
//Fire that second activity
                if (!mLastQuery.contains("No result found")) {
                    startActivity(intent, options.toBundle());
                    floatingSearchView.setSearchBarTitle(mLastQuery);
                    floatingSearchView.clearSearchFocus();
                }

            }

            @Override
            public void onSearchAction(String currentQuery) {
                List<ItemSuggestions> filtereditems = new ArrayList<>();
                for (ItemSuggestions i : items) {
                    if (i.getBody().contains(currentQuery)) {
                        filtereditems.add(i);

                    }

                }

                if (filtereditems.size() < 1)
                    filtereditems.add(new ItemSuggestions("No result found"));
                floatingSearchView.swapSuggestions(filtereditems);

            }
        });
    }
   /* void onAttachSearchViewToDrawer(FloatingSearchView searchView) {
        searchView.attachNavigationDrawerToMenuButton(drawer);
    }*/

    public boolean onActivityBackPress() {
        //if mSearchView.setSearchFocused(false) causes the focused search
        //to close, then we don't want to close the activity. if mSearchView.setSearchFocused(false)
        //returns false, we know that the search was already closed so the call didn't change the focus
        //state and it makes sense to call supper onBackPressed() and close the activity

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            floatingSearchView.setSearchBarTitle(matches.get(0).toString());

        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       /* int id = item.getItemId();
        floatingSearchView.setSearchBarTitle(items.get(id).getBody());
        floatingSearchView.clearSearchFocus();

        //noinspection SimplifiableIfStatement*/


        return super.onOptionsItemSelected(item);
    }


    private void fadeDimBackground(int from, int to, Animator.AnimatorListener listener) {
        ValueAnimator anim = ValueAnimator.ofInt(from, to);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int value = (Integer) animation.getAnimatedValue();
                mDimDrawable.setAlpha(value);
            }
        });
        if (listener != null) {
            anim.addListener(listener);
        }
        anim.setDuration(200);
        anim.start();
    }


  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/




   /*
        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                ItemSuggestions ItemSuggestions1 = (ItemSuggestions) searchSuggestion;


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
                    floatingSearchView.swapSuggestions(items);

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

                ActivityOptions options = ActivityOptions.makeScaleUpAnimation(floatingSearchView.getRootView(), 0, 0, 280, 280);
//Fire that second activity
                if (!mLastQuery.contains("No result found")) {
                    startActivity(intent, options.toBundle());
                    floatingSearchView.setSearchBarTitle(mLastQuery);
                    floatingSearchView.clearSearchFocus();
                }

            }
*/


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

                        items = new ArrayList<>();

                        search_list = new ArrayList<String>(jArray.length());
                        for (int i = 0; i < jArray.length(); i++) {
                            Log.d("JarrayLength", jArray.length() + "");
                            json_data = jArray.getJSONObject(i);

                            item_name = json_data.getString("itemName");
                            Log.d(item_name, "item");
                            search_list.add(item_name);
                            items.add(new ItemSuggestions(item_name));
                        }
                        floatingSearchView.swapSuggestions(items);
                        Log.d("addedList", search_list + "");
                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(SearchActivity.this, HomeActivity.class));
        finish();
    }
}
