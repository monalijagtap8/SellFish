package com.example.android.sellfish;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by user on 24/1/18.
 */

public class AdapterViewCart extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    List<DataViewCart> data = Collections.emptyList();
    MyHolder myHolder;
    VolleyRequest urlRequest;
    ArrayList<String> list;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String user_id;
    VolleyRequest volleyRequest;
    private Context context;
    private LayoutInflater inflater;

    public AdapterViewCart(Context context, List<DataViewCart> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        myHolder = (MyHolder) holder;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.container_viewcart, parent, false);
        MyHolder holder = new MyHolder(view);
        ButterKnife.inject(this, view);
        sp = context.getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        editor = sp.edit();
        return holder;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final MyHolder myHolder = (MyHolder) holder;
        final int pos = position;
        final DataViewCart item_data = data.get(position);
        Log.d("position", position + "");

        myHolder.txt_name.setText(item_data.name);
        myHolder.txt_type.setText(item_data.type);
        myHolder.txt_quantity.setText(item_data.quantity + "");
        myHolder.txt_price.setText(item_data.price + "");

        Glide.with(context).load("http://192.168.0.110:8001/routes/server/" + item_data.image).asBitmap().override(600, 600)
                .placeholder(null).listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                Log.d("image", "http://192.168.0.110:8001/routes/server/" + item_data.image);
                myHolder.imageView.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                // myHolder.imageView.setVisibility(View.GONE);
                Log.d("image", "http://192.168.0.110:8001/routes/server/" + item_data.image);
                return false;
            }
        }).error(null).into(myHolder.imageView);

        myHolder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int quantity = Integer.parseInt(myHolder.txt_quantity.getText() + "");
                myHolder.txt_quantity.setText(++quantity + "");
            }
        });

        myHolder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int quantity = Integer.parseInt(myHolder.txt_quantity.getText() + "");
                if (quantity > 1)
                    myHolder.txt_quantity.setText(--quantity + "");
            }
        });

        myHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volleyRequest = VolleyRequest.getObject();
                volleyRequest.setContext(context);
                Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/removeFromCart.rfa.php?user_id=" + item_data.user_id + "&item_id=" + item_data.item_id);
                volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/removeFromCart.rfa.php?user_id=" + item_data.user_id + "&item_id=" + item_data.item_id);
                volleyRequest.getResponse(new ServerCallback() {
                    @Override
                    public void onSuccess(String response) {

                        Log.d("ResponseDelete", response);

                        Activity a = (Activity) context;
                        a.recreate();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        TextView txt_name, txt_type, txt_quantity, txt_price;
        Button btn_add, btn_remove, btn_delete;
        ImageView imageView;

        public MyHolder(View itemView) {
            super(itemView);

            btn_add = itemView.findViewById(R.id.btn_add);
            btn_remove = itemView.findViewById(R.id.btn_remove);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_type = itemView.findViewById(R.id.txt_type);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_quantity = itemView.findViewById(R.id.txt_quantity);
            imageView = itemView.findViewById(R.id.img_viewCart);

        }
    }
}




