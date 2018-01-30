package com.example.android.sellfish;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Description extends AppCompatActivity {
    String name, image, price, available, desc, item_id, user_id;
    @InjectView(R.id.txt_name)
    TextView txt_name;
    @InjectView(R.id.txt_price)
    TextView txt_price;
    @InjectView(R.id.txt_availability)
    TextView txt_available;
    @InjectView(R.id.txt_description)
    TextView txt_description;
    @InjectView(R.id.img_view)
    ImageView imgView;
    @InjectView(R.id.btn_addToCart)
    Button btnAddToCart;
    VolleyRequest volleyRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        ButterKnife.inject(this);
        name = getIntent().getStringExtra("NAME");
        image = getIntent().getStringExtra("IMAGE");
        price = getIntent().getStringExtra("PRICE");
        item_id = getIntent().getStringExtra("ITEM_ID");
        user_id = getIntent().getStringExtra("USER_ID");
        available = getIntent().getStringExtra("AVAILABILITY");
        desc = getIntent().getStringExtra("DESCRIPTION");
        Log.d(name, "name");
        Log.d(user_id, "user");
        txt_name.setText(name);
        txt_price.setText(price);
        txt_available.setText(available);
        txt_description.setText(desc);


        Glide.with(Description.this).load("http://192.168.0.110:8001/routes/server/" + image).asBitmap().override(600, 600)
                .placeholder(null).listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                Log.d("image", "http://192.168.0.110:8001/routes/server/" + image);
                imgView.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                // myHolder.imageView.setVisibility(View.GONE);
                Log.d("image", "http://192.168.0.110:8001/routes/server/" + image);
                return false;
            }
        }).error(null).into(imgView);

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volleyRequest = VolleyRequest.getObject();
                volleyRequest.setContext(Description.this);
                Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/addToCart.rfa.php?user_id=" + user_id + "&item_id=" + item_id);
                volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/addToCart.rfa.php?user_id=" + user_id + "&item_id=" + item_id);
                volleyRequest.getResponse(new ServerCallback() {
                    @Override
                    public void onSuccess(String response) {

                        Log.d("addToCart", response);
                        Toast.makeText(Description.this, "clicked", Toast.LENGTH_LONG).show();


                    }
                });
            }
        });

    }
}