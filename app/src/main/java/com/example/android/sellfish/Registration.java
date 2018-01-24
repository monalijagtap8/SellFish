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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.inject(this);
    }


    @OnClick({R.id.btnRegister})
    public void onClick(final View view) {

        switch (view.getId()) {

            case R.id.btnRegister:

                      name=edtName.getText().toString();
                        if(name.length()==0)
                        {
                            flag=1;
                        }
                        email=edtEmail.getText().toString();
                        if(email.length()==0)
                        {
                            flag=1;
                        }
                        if(phone.length()==0)
                        {
                            flag=1;
                        }
                        if (password.length()==0)
                        {
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
        Log.d("checkData: ", "http://sansmealbox.com/admin/routes/server/app/checkUserInfo.rfa.php?auth_id");
       volleyRequest.setUrl("http://sansmealbox.com/admin/routes/server/app/checkUserInfo.rfa.php?auth_i");
           volleyRequest.getResponse(new ServerCallback() {
                @Override
                public void onSuccess(String response) {

                }
            });
        }
}
