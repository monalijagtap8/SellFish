package com.example.android.sellfish;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class Registration extends AppCompatActivity {

   @InjectView(R.id.edtName)EditText edtName;
    @InjectView(R.id.edtPhone)EditText edtPhone;
    @InjectView(R.id.edtEmail)EditText edtEmail;
    @InjectView(R.id.edtPassword)EditText edtPassword;
    @InjectView(R.id.btnRegister)Button btnRegister;
    int flag=0;
    String name, email, phone, password, pwd;
    VolleyRequest volleyRequest;
    String parent_activity;
    Intent intent;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.inject(this);
        sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        parent_activity = getIntent().getStringExtra("PARENT_ACTIVITY");
        if (parent_activity.equals("UserProfile")) {
            edtPassword.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.btnRegister,R.id.edtName,R.id.edtEmail,R.id.edtPhone,R.id.edtPassword})
    public void onClick(final View view) {

        switch (view.getId()) {
            case R.id.btnRegister:

                name = edtName.getText().toString().trim();
                        if(name.length()==0)
                        {
                            edtName.setError("Please enter name");
                            flag=1;
                        }
                email = edtEmail.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                        if(emailPattern.matches(email)||email.length()==0)
                        {
                            edtEmail.setError("Please enter email");
                            flag=1;
                        }
                phone = edtPhone.getText().toString().trim();
                        if(phone.length()==0||phone.length()<10)
                        {
                            edtPhone.setError("Please enter phone");
                            flag=1;
                        }
                password = edtPassword.getText().toString().trim();
                if (!parent_activity.equals("UserProfile")) {
                    if (password.length() == 0) {
                        edtPassword.setError("Please enter password");
                        flag = 1;
                    }
                }
                //flag=0;
                        if(flag==0)
                        {
                            register();
                        }
                      break;
        }
    }
    public void register() {

       volleyRequest=VolleyRequest.getObject();
        volleyRequest.setContext(getApplicationContext());
        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/userData.rfa.php?name="+name+"&email="+email+"&phone="+phone+"&password="+password);
       volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/userData.rfa.php?name="+name+"&email="+email+"&phone="+phone+"&password="+password);
           volleyRequest.getResponse(new ServerCallback()
           {
                @Override
                public void onSuccess(String response)
                {
                        Log.d("Response",response);
                        if(response.contains("OK")) {
                            intent=new Intent(Registration.this,LoginActivity.class);
                            finish();
                            startActivity(intent);
                            Toast.makeText(Registration.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Registration.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                }
            });
        }
}
