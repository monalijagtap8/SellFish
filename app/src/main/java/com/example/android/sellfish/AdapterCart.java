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
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import butterknife.ButterKnife;

/**
 * Created by user on 24/1/18.
 */

public class AdapterCart extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    List<DataCart> data = Collections.emptyList();
    MyHolder myHolder;
    VolleyRequest urlRequest;
    ArrayList<String> list;
    private Context context;
    private LayoutInflater inflater;

    // create constructor to innitilize context and data sent frm MainActivity
    public AdapterCart(Context context, List<DataCart> data) {
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
        View view = inflater.inflate(R.layout.container_adapter, parent, false);
        MyHolder holder = new MyHolder(view);
        ButterKnife.inject(this, view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final MyHolder myHolder = (MyHolder) holder;
        final int pos = position;
        final DataCart item_data = data.get(position);
        Log.d("position", position + "");

            myHolder.txt_name.setText(item_data.name);
            myHolder.btn_description.setText("Description");
            myHolder.btn_addToCart.setText("add to cart");

        Glide.with(context).load("http://192.168.0.110:8001/routes/server/"+item_data.image).asBitmap().override(600, 600)
                .placeholder(null).listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                Log.d( "image","http://192.168.0.110:8001/routes/server/"+item_data.image);
                myHolder.imageView.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
               // myHolder.imageView.setVisibility(View.GONE);
                Log.d( "image","http://192.168.0.110:8001/routes/server/"+item_data.image);
                return false;
            }
        }).error(null).into(myHolder.imageView);
    }





    @Override
    public int getItemCount() {
        return data.size();
    }



    class MyHolder extends RecyclerView.ViewHolder {
        TextView txt_name;
        Button btn_addToCart,btn_description;
        ImageView imageView;

        public MyHolder(View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            btn_addToCart = itemView.findViewById(R.id.btn_addToCart);
            btn_description= itemView.findViewById(R.id.btn_description);
            imageView= itemView.findViewById(R.id.image_view);

        }
    }
}




