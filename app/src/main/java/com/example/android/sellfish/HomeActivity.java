package com.example.android.sellfish;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    public static List<ItemSuggestions> items;
    @InjectView(R.id.img_viewCart)
    ImageView viewCart;
    @InjectView(R.id.txt_fish)
    Button btn_fish;
    @InjectView(R.id.txt_poultry)
    Button btn_poultry;
    @InjectView(R.id.txt_mutton)
    Button btn_mutton;
    @InjectView(R.id.txt_deals)
    Button btn_deals;
    @InjectView(R.id.floating_search_view)
    FloatingSearchView floatingSearchView;
    @InjectView(R.id.cartCount)
    TextView cartCount;
    @InjectView(R.id.layout_fish)
    RelativeLayout layout_fish;
    @InjectView(R.id.layout_mutton)
   RelativeLayout layout_mutton;
    @InjectView(R.id.layout_poultry)
    RelativeLayout layout_poultry;
    @InjectView(R.id.layout_deals)
   RelativeLayout layout_deals;
    @InjectView(R.id.btn_location)
    TextView btn_location;
    @InjectView(R.id.search_list)
    ListView listView;
    @InjectView(R.id.slider)
    SliderLayout sliderShow;
    @InjectView(R.id.imageview_advt)
    ImageView imageView_advt;
    @InjectView(R.id.txtNotification)
    TextView txtNotification;
    AdapterCategories adapter;
    List<DataCategories> data;
    Animation animation;
    RecyclerView recyclerView;
    VolleyRequest volleyRequest;
    JSONArray jArray;
    JSONObject json_data;
    String user_id, item_name, name, desc, image, price;
    int item_id;
    SharedPreferences sp;
    long back_pressed = 0;
    DrawerLayout drawer;
    Toast toast;
    SharedPreferences.Editor editor;
    Intent intent;
    int imageCount = 2131165282;
    List<String> search_list;
    String loc;
    MyFirebaseMessagingService messagingService;
    private View mDifloatingSearchViewBackground;
    private ColorDrawable mDimDrawable;
    private String mLastQuery = "Search...", TAG;

    ImageView fishcat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        editor = sp.edit();
        startService(new Intent(getApplicationContext(), MyFirebaseMessagingService.class));
        user_id = sp.getString("USER_ID", "");
        loc=sp.getString("LOC","");
        if(loc.isEmpty())
        {
            btn_location.setText("Location");
        }
        else
        {
            btn_location.setText(loc);
        }
        for (int i = 1; i <= 3; i++) {
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            textSliderView.image(imageCount);
            imageCount++;
            sliderShow.addSlider(textSliderView);
            animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
            Log.d(R.drawable.ay1 + " " + R.drawable.ay2 + " " + R.drawable.ay3, "fvalue");

        }
        imageView_advt.setImageResource(R.drawable.advt1);


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


        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /*volleyRequest=VolleyRequest.getObject();
        volleyRequest.setContext(getApplicationContext());
        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/fetchTypes.rfa.php");
        volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/fetchTypes.rfa.php");
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
                            DataCategories item_data = new DataCategories();
                            item_data.type = json_data.getString("type");
                            item_data.image=json_data.getString("min(image_path)");
                            Log.d( jArray+"", "data*************");
                            data.add(item_data);
                        }
                        recyclerView = findViewById(R.id.Listmenu);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));
                        recyclerView.setNestedScrollingEnabled(false);
                        recyclerView.setHasFixedSize(false);
                        adapter = new AdapterCategories(HomeActivity.this, data);
                        Log.d(data+"", "data*************");
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {

                        e.printStackTrace();

                    }
                }
            }
        });*/
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(HomeActivity.this);
                builderSingle.setTitle("Select City");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(HomeActivity.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("Alandi");
                arrayAdapter.add("Dighi");
                arrayAdapter.add("Bhosari");
                arrayAdapter.add("Sanghavi");
                arrayAdapter.add("Talegaon");
                arrayAdapter.add("Pimpri");
                arrayAdapter.add("Chinchwad");
                arrayAdapter.add("Moshi");
                arrayAdapter.add("Shivaji Nagar");
                arrayAdapter.add("wadi");

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       /* String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(HomeActivity.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();*/
                        loc=arrayAdapter.getItem(which);
                        editor.putString("LOC",loc);
                        editor.commit();
                        btn_location.setText(loc);
                    }
                });
                builderSingle.show();
            }
        });
        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(HomeActivity.this, ViewCart.class);
                finish();
                startActivity(intent);
            }
        });

        layout_fish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                intent= new Intent(HomeActivity.this, SubCategoryActivity.class);
                intent.putExtra("Activity","Fish");
                startActivity(intent);
                HomeActivity.this.finish();
            }

        });

        layout_mutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(HomeActivity.this, SubCategoryActivity.class);
                intent.putExtra("Activity","Mutton");
                startActivity(intent);
                finish();
            }

        });
        layout_poultry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(HomeActivity.this, SubCategoryActivity.class);
                intent.putExtra("Activity","Poultry");
                startActivity(intent);
                finish();
            }

        });
        layout_deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(HomeActivity.this, SubCategoryActivity.class);
                intent.putExtra("Activity","Deals");
                startActivity(intent);
                finish();
            }

        });
        btn_fish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(HomeActivity.this, SubCategoryActivity.class);
                intent.putExtra("Activity","Fish");
                startActivity(intent);
                finish();
            }

        });

        btn_mutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(HomeActivity.this, SubCategoryActivity.class);
                intent.putExtra("Activity","Mutton");
                startActivity(intent);
                finish();
            }


        });
        btn_poultry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(HomeActivity.this, SubCategoryActivity.class);
                intent.putExtra("Activity","Poultry");
                startActivity(intent);
                finish();
            }

        });
        btn_deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(HomeActivity.this, SubCategoryActivity.class);
                intent.putExtra("Activity","Deals");
                startActivity(intent);
                finish();
            }

        });

        floatingSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFocusCleared() {

                ObjectAnimator anim = ObjectAnimator.ofFloat(floatingSearchView, "translationY",
                        0, 0);
                anim.setDuration(350);
                anim.start();
                floatingSearchView.setSearchBarTitle(mLastQuery);
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

            }
            else {

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
        } else if (id == R.id.nav_fish) {
            intent=new Intent(HomeActivity.this, SubCategoryActivity.class);
            intent.putExtra("Activity","Fish");
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_poultry) {
            intent=new Intent(HomeActivity.this, SubCategoryActivity.class);
            intent.putExtra("Activity","Poultry");
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_mutton) {
            intent=new Intent(HomeActivity.this, SubCategoryActivity.class);
            intent.putExtra("Activity","Mutton");
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_deals) {
            intent=new Intent(HomeActivity.this, SubCategoryActivity.class);
            intent.putExtra("Activity","Deals");
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_favourite) {
            intent = new Intent(HomeActivity.this, FavouritesActivity.class);
            finish();
            startActivity(intent);
        } else if (id == R.id.nav_find_product) {
            intent = new Intent(HomeActivity.this, FindUrProduct.class);
            finish();
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class MyFirebaseMessagingService extends FirebaseMessagingService {
        @SuppressLint("WrongThread")
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {

            Log.d("fcm", "received notification");
            if (remoteMessage.getNotification() != null) {
                Log.d("Message", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }
            sendNotification(remoteMessage.getNotification().getTitle());
        }

        @SuppressLint("NewApi")
        private void sendNotification(String messageBody) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(messageBody)
                    .setAutoCancel(false)
                    .setSound(defaultSoundUri);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1, notificationBuilder.build());
          /*  Log.d("Notify",notificationManager.getActiveNotifications().toString());
            txtNotification.setText( notificationManager.getActiveNotifications().toString());*/
        }
    }

}
