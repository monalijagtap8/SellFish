package com.example.android.sellfish;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UserProfile extends AppCompatActivity {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Intent intent;
    VolleyRequest urlRequest;
    String id, name, email, phone, password;
    // AlertDialog alertDialog;
    @InjectView(R.id.txtName)
    TextView txtName;
    @InjectView(R.id.txtPhone)
    TextView txtPhone;
    @InjectView(R.id.txtEmail)
    TextView txtEmail;
    @InjectView(R.id.imgEdit)
    ImageView imgEdit;
    boolean logged_in;
    AlertDialog.Builder alert;
    CardView cardAddress, cardPhone, cardEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.inject(this);
        sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        logged_in = sp.getBoolean("LOGGED_IN", false);
        Log.d("Login@@@", logged_in + "");
        editor = sp.edit();
        cardPhone = findViewById(R.id.cardPhone);
        // cardAddress = findViewById(R.id.cardAddress);
        cardEmail = findViewById(R.id.cardEmail);
        if (!sp.getBoolean("LOGGED_IN", false)) {
            intent = new Intent(UserProfile.this, LoginActivity.class);
            intent.putExtra("PARENT_ACTIVITY_NAME", "UserProfile");
            finish();
            startActivity(intent);
        } else {

            getData();
        }
    }

    @OnClick({R.id.txtLogin, R.id.imgEdit})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.txtLogin:

                alert = new AlertDialog.Builder(UserProfile.this);
                alert.setMessage("Are you sure you want to logout?");
                alert.setCancelable(false);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor = sp.edit();
                        editor.putBoolean("LOGGED_IN", false);
                        editor.commit();
                        boolean login = sp.getBoolean("LOGGED_IN", false);
                        Log.d("LOgin***", login + "");
                        intent = new Intent(UserProfile.this, HomeActivity.class);
                        finish();
                        startActivity(intent);
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog ad1 = alert.create();
                ad1.show();

                break;
            case R.id.imgEdit:
                intent = new Intent(UserProfile.this, Registration.class);
                intent.putExtra("PARENT_ACTIVITY", "UserProfile");
                finish();
                startActivity(intent);
                break;
        }
    }

    public void getData() {

        email = sp.getString("EMAIL", "");
        Log.d("Id", email);
        urlRequest = VolleyRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/fetchUserProfile.rfa.php?email_id=" + email);
        urlRequest.setUrl("http://192.168.0.110:8001/routes/server/app/fetchUserProfile.rfa.php?email_id=" + email);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d("Response*", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    name = jsonObject.getString("name");
                    email = jsonObject.getString("email");
                    phone = jsonObject.getString("phone");
                    password = jsonObject.getString("password");

                    Log.d("Name", name);

                    txtName.setText(name);
                    txtPhone.setText(phone);
                    txtEmail.setText(email);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
