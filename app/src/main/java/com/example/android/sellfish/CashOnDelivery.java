package com.example.android.sellfish;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CashOnDelivery extends AppCompatActivity {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String name, phone, email;
    @InjectView(R.id.edt_name)
    EditText edtName;
    @InjectView(R.id.edt_phone)
    EditText edtPhone;
    @InjectView(R.id.edt_address)
    EditText edtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_on_delivery);
        ButterKnife.inject(this);
        sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        name = sp.getString("NAME", "");
        phone = sp.getString("PHONE", "");
        email = sp.getString("EMAIL", "");
        edtName.setText(name);
        edtPhone.setText(phone);

    }
}
