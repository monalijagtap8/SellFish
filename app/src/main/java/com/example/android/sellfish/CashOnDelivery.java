package com.example.android.sellfish;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

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
    @InjectView(R.id.img_back)
    ImageView imgBack;
    VolleyRequest volleyRequest;
    String user_id, user_name, itemname, price;
    JSONObject jsonObject, jObj, jObj1, jj;
    JSONArray jsonArray1, jArray;
    RequestQueue requestQueue;
    JSONObject json;
    View view;
    LinearLayout ll;
    Bitmap bitmap;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_on_delivery);
        ButterKnife.inject(this);
        sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(CashOnDelivery.this);
        name = sp.getString("NAME", "");
        phone = sp.getString("PHONE", "");
        email = sp.getString("EMAIL", "");
        address = sp.getString("ADDRESS", "");
        edtName.setText(name);
        edtPhone.setText(phone);
        edtAddress.setText(address);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnOrdrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    jsonObject = new JSONObject(getIntent().getStringExtra("OBJECT"));
                    /* if(jsonObject.toString().contains("\\"))
                     {
                         rr=jsonObject.toString().replaceAll("\\\\","");
                         jsonObject=new JSONObject(rr);
                         Log.d("GG",jsonObject+"");
                     }*/
                    /* rr=jsonObject.toString().replaceAll("\\\\","");
                     jj=new JSONObject(rr);*/
                    Log.d(jsonObject + "", "JJJ");
                    requestQueue = Volley.newRequestQueue(CashOnDelivery.this);
                    Log.d("Url1", "http://192.168.0.110:8001/routes/server/app/myOrder.rfa.php");
                    final JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                            Request.Method.POST, "http://192.168.0.110:8001/routes/server/app/myOrder.rfa.php", jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("ResponseODA", response.toString());
                                    try {
                                        fetchOrderedItems(jsonObject);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(CashOnDelivery.this, "Your order placed successfully..!", Toast.LENGTH_LONG).show();
                                    // startActivity(new Intent(CashOnDelivery.this, HomeActivity.class));
                                    // finish();
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

    public void fetchOrderedItems(final JSONObject jsonObject) throws JSONException {

        Log.d("bat", jsonObject.getJSONArray("jsonObject") + "");
        jsonArray1 = jsonObject.getJSONArray("jsonObject");
        jObj = jsonArray1.getJSONObject(0);
        user_id = jObj.getString("user_id");
        jObj.put("item_id", "-1");
        jArray = new JSONArray();
        jArray.put(jObj.toString());
        json = new JSONObject();
        json.put("jsonObject", jArray);
        Log.d(json + "", "jobj ");

        Log.d("gal", jObj.getString("user_id") + "");
        Log.d("object", jsonObject + "");

        Log.d("HH", json + "");
        Log.d("Url1", "http://192.168.0.110:8001/routes/server/app/myOrder.rfa.php");

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, "http://192.168.0.110:8001/routes/server/app/myOrder.rfa.php", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ResponseODA11", response.toString());
                        try {
                            JSONObject jj = new JSONObject(response.toString());
                            jsonArray1 = jj.getJSONArray("response");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                jObj = jsonArray1.getJSONObject(0);
                                user_name = jObj.getString("name");
                                itemname = jObj.getString("itemName");
                                price = jObj.getString("price");
                                Log.d("Name", user_name + itemname + price);
                            }
                            alertShow();

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: ", error.getMessage());
            }
        });
        requestQueue.add(jsonObjReq);
    }

    public Bitmap TakeScreenShot(View view) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = view; //getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
        return bitmap;
    }

    public void alertShow() {
        LayoutInflater li = LayoutInflater.from(CashOnDelivery.this);

        //Creating a view to get the dialog box
        view = li.inflate(R.layout.dialog_ordered_details, null);
        ll = view.findViewById(R.id.linearInVoice);
        TextView txtName = view.findViewById(R.id.txtName);
        TextView txtItemName = view.findViewById(R.id.txtItems);
        TextView txtPrice = view.findViewById(R.id.txtPrice);
        Button btnScreenShot = view.findViewById(R.id.btnScreenShot);
        txtName.setText(user_name);
        txtItemName.setText(itemname);
        txtPrice.setText(price);
        btnScreenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap screenShot = TakeScreenShot(ll);

                if (ContextCompat.checkSelfPermission(CashOnDelivery.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Save the screenshot on device gallery
                    MediaStore.Images.Media.insertImage(
                            getContentResolver(),
                            screenShot,
                            "Image",
                            "Captured ScreenShot"
                    );
                }
                intent = new Intent(CashOnDelivery.this, HomeActivity.class);
                // Notify the user that screenshot taken.
                Toast.makeText(getApplicationContext(), "Screen Captured.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(CashOnDelivery.this);
        //Adding our dialog box to the view of alert dialog
        alert.setView(view);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

}








