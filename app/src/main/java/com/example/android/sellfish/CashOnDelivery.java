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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CashOnDelivery extends AppCompatActivity {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String name, phone, email, address;
    @InjectView(R.id.edt_name)
    EditText edtName;
    @InjectView(R.id.edt_phone)
    EditText edtPhone;
    @InjectView(R.id.edt_address)
    EditText edtAddress;
    @InjectView(R.id.btn_order)
    Button btnOrdrer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_on_delivery);
        ButterKnife.inject(this);
        sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        name = sp.getString("NAME", "");
        phone = sp.getString("PHONE", "");
        email = sp.getString("EMAIL", "");
        address = sp.getString("ADDRESS", "");
        edtName.setText(name);
        edtPhone.setText(phone);
        edtAddress.setText(address);
        btnOrdrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("OBJECT"));
                    Log.d(jsonObject + "", "JJJ");
                    RequestQueue requestQueue = Volley.newRequestQueue(CashOnDelivery.this);
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                            Request.Method.POST, "http://192.168.0.110:8001/routes/server/app/myOrder.rfa.php", jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("ResponseODA", response.toString());
                                    Toast.makeText(CashOnDelivery.this, "Your order placed successfully..!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(CashOnDelivery.this, HomeActivity.class));
                                    finish();

                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d("Error: ", error.getMessage());

                        }
                    });
                    requestQueue.add(jsonObjReq);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
