package com.example.android.sellfish;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.Collections;
import java.util.List;

/**
 * Created by user on 15/2/18.
 */

public class AdapterCategories extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    List<DataCategories> data = Collections.emptyList();
    MyHolder myHolder;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String user_id;
    VolleyRequest volleyRequest;
    Intent intent;
    private Context context;
    private LayoutInflater inflater;

    public AdapterCategories(Context context, List<DataCategories> data) {
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
        View view = inflater.inflate(R.layout.container_categories, parent, false);
        MyHolder holder = new MyHolder(view);

        sp = context.getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        editor = sp.edit();
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final MyHolder myHolder = (MyHolder) holder;
        final int pos = position;
        final DataCategories item_data = data.get(position);
        Log.d("position", position + "");

        myHolder.txt_name.setText(item_data.type);

        user_id = sp.getString("USER_ID", "");
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
        myHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(context, SubCategoryActivity.class));

            }
        });

    }

    public void refresh() {
        Intent i = ((Activity) context).getIntent();
        ((Activity) context).finish();
        context.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView txt_name;
        ImageView imageView;
        CardView cardView;

        public MyHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            imageView = itemView.findViewById(R.id.image_view);
            cardView = itemView.findViewById(R.id.recycleCardView);
        }
    }
}




