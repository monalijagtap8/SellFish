package com.example.android.sellfish;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @InjectView(R.id.edtName1)EditText edtName;
    @InjectView(R.id.edtPassword1)EditText edtPassword;
    @InjectView(R.id.btnLogin)Button btnLogin;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    VolleyRequest volleyRequest;
    String email,passowrd,name,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        try {
            File f = new File("/data/data/com.xoxytech.ostello/shared_prefs/YourSharedPreference.xml");
            if (f.exists())
            {
                Log.d("TAG", "SharedPreferences Name_of_your_preference : exist");
                 sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);

            } else
                Log.d("TAG", "Setup default preferences");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @OnClick({R.id.btnLogin})
    public void onClick(final View view) {

        switch (view.getId()) {

            case R.id.btnLogin:
                login();
                break;

        }

    }
    public void login()
    {
        email=edtName.getText().toString();
        passowrd=edtPassword.getText().toString();

        volleyRequest=VolleyRequest.getObject();
        volleyRequest.setContext(getApplicationContext());

        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/fetchUserData.rfa.php?email="+email+"&password="+passowrd);
        volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/fetchUserData.rfa.php?email="+email+"&password="+passowrd);
        volleyRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response)
            {
                Log.d("Response",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    name=jsonObject.getString("name");
                    email=jsonObject.getString("email");
                    phone=jsonObject.getString("phone");

                    sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
                    editor=sp.edit();
                    editor.putString("NAME",name);
                    editor.putString("PHONE",phone);
                    editor.putString("EMAIL",email);
                    editor.commit();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}
