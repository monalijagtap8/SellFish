package com.example.android.sellfish;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by user on 24/1/18.
 */

public class AdapterCart extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    List<DataCart> data = Collections.emptyList();
    MyHolder myHolder;
    List<DataItem> data1 = Collections.emptyList();
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int item_id;
    VolleyRequest volleyRequest;
    Intent intent;
    int  currentPosition;
    MarineFragment marineFragment;
    private Context context;
    private LayoutInflater inflater;
    PopupMenu popup;
    JSONObject json_data;
    JSONArray jArray;
    DataItem data_item;


    public AdapterCart(Context context, List<DataCart> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }
    public AdapterCart( List<DataItem> data) {
        this.data1 = data;
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
        sp = context.getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        editor = sp.edit();
        return holder;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        myHolder = (MyHolder) holder;
        final int pos = position;
        final DataCart item_data = data.get(position);
        Log.d("position", position + "");
        final String user_id = sp.getString("USER_ID", "");
        final int id1= item_data.getId();
         currentPosition = 0;
          checkFavourie(user_id,21);
      /*  for (DataItem dataItem:data1) {
            Log.d("m called","****");
            item_id=dataItem.getId();
          checkFavourie(user_id,item_id);
            currentPosition++;
        }*/
        //item_id=data_item.getId();
       // checkFavourie(user_id,item_id);
        myHolder.txt_name.setText(item_data.name);
//        myHolder.description.setText("Description");
//        myHolder.btn_addToCart.setText("add to cart");

        Glide.with(context).load("http://192.168.0.110:8001/routes/server/" + item_data.image).asBitmap().override(600, 600)
                .placeholder(null).listener(new RequestListener<String, Bitmap>()
        {
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

        myHolder.toggleFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                setFavourite(user_id,id1);
            }
        });

        myHolder.addtocart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (sp.getBoolean("LOGGED_IN", false)) {
                    volleyRequest = VolleyRequest.getObject();
                    volleyRequest.setContext(context);
                    Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/addToCart.rfa.php?user_id=" + user_id + "&item_id=" + id1);
                    volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/addToCart.rfa.php?user_id=" + user_id + "&item_id=" + id1);
                    volleyRequest.getResponse(new ServerCallback() {
                        @Override
                        public void onSuccess(String response) {
                            Log.d("Responsecart", response);
                            //((HomeActivity) context).getItemCount();
                            ((SubCategoryActivity) context).getItemCount1();

                            // refresh();
                        }
                    });
                } else {
                    intent = new Intent(context, TabActivity.class);
                    ((Activity) context).finish();
                    context.startActivity(intent);
                }
            }
        });

        myHolder.description.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(final View view) {

                 popup = new PopupMenu(context, myHolder.description); //you can use image button
               // as btnSettings on your GUI after
               //clicking this button pop up menu will be shown
                popup.getMenuInflater().inflate(R.menu.details_bt_menu, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.action_description:

                                intent = new Intent(context, Description.class);
                                intent.putExtra("NAME", item_data.name);
                                intent.putExtra("IMAGE", item_data.image);
                                intent.putExtra("ITEM_ID", id1 + "");
                                intent.putExtra("USER_ID", user_id + "");
                                Log.d(user_id, "intent");
                                intent.putExtra("PRICE", item_data.price + "");
                                intent.putExtra("AVAILABILITY", "available");
                                intent.putExtra("DESCRIPTION", item_data.desc);
                                view.getContext().startActivity(intent);
                                break;

                            default:
                                break;
                        }
                        return false;
                    }
                });

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
        ImageView imageView,description,addtocart;
        RelativeLayout relativeLayout;
        ToggleButton toggleFavourite;

        public MyHolder(View itemView)
        {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            addtocart = itemView.findViewById(R.id.add_to_cart);
            description = itemView.findViewById(R.id.description);
            //favourite = itemView.findViewById(R.id.add_to_favourite);
            imageView = itemView.findViewById(R.id.image_view);
            relativeLayout = itemView.findViewById(R.id.relativeAdapter);
            toggleFavourite=itemView.findViewById(R.id.toggleFavourite);
        }
    }

    public void setFavourite(String user_id1,int id)
    {
        if (sp.getBoolean("LOGGED_IN", false))
        {
          // Log.d("Id",id1+"");
            volleyRequest = VolleyRequest.getObject();
            volleyRequest.setContext(context);
            Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/addToFavourites.rfa.php?user_id=" + user_id1 + "&item_id=" +id);
            volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/addToFavourites.rfa.php?user_id=" + user_id1 + "&item_id=" + id);
            volleyRequest.getResponse(new ServerCallback() {
                @Override
                public void onSuccess(String response) {

                    Log.d("cbcjhe", response);
                    if (response.contains("ADDED")) {
                        myHolder.toggleFavourite.setChecked(true);
                        Toast.makeText(context, "Item added to favourite", Toast.LENGTH_LONG).show();
                    } else {
                        myHolder.toggleFavourite.setChecked(false);
                        Toast.makeText(context, "Item removed from favourite", Toast.LENGTH_LONG).show();
                    }

                }
            });
        } else {
            intent = new Intent(context, TabActivity.class);
            ((Activity)context).finish();
            context.startActivity(intent);
        }
    }
    public void checkFavourie(String user_id, final int item_id) {
        volleyRequest = VolleyRequest.getObject();
        volleyRequest.setContext(context);
        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/favourites.rfa.php?user_id=" + user_id + "&item_id=" + item_id);
        volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/favourites.rfa.php?user_id=" + user_id + "&item_id=" + item_id);
        volleyRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) {

                Log.d("checkF", response);

                    if (response.contains("EXISTS")) {
                        myHolder.toggleFavourite.setChecked(true);
                    } else {
                        myHolder.toggleFavourite.setChecked(false);
                    }

            }
        });
    }


}




