package com.example.android.sellfish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    String name,email,phone,password;
    VolleyRequest volleyRequest;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.inject(this);
    }


    @OnClick({R.id.btnRegister,R.id.edtName,R.id.edtEmail,R.id.edtPhone,R.id.edtPassword})
    public void onClick(final View view) {

        switch (view.getId()) {

            case R.id.btnRegister:

                      name=edtName.getText().toString();
                        if(name.length()==0)
                        {
                            edtName.setError("Please enter name");
                            flag=1;
                        }
                        email=edtEmail.getText().toString();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                        if(emailPattern.matches(email)||email.length()==0)
                        {
                            edtEmail.setError("Please enter email");
                            flag=1;
                        }
                        phone=edtPhone.getText().toString();
                        if(phone.length()==0||phone.length()<10)
                        {
                            edtPhone.setError("Please enter phone");
                            flag=1;
                        }
                        password=edtPassword.getText().toString();
                        if (password.length()==0)
                        {
                            edtPassword.setError("Please enter password");
                            flag=1;
                        }
                        flag=0;
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
                        if(response.contains("OK"))
                        {
                            intent=new Intent(Registration.this,LoginActivity.class);
                            startActivity(intent);
                        }
                }
            });
        }
}
