package com.example.android.sellfish;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FrozenFragment extends Fragment {

    AdapterCart adapter;
    List<DataCart> data;
    RecyclerView recyclerView;
    VolleyRequest volleyRequest;
    JSONArray jArray;
    JSONObject json_data;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frozen, container, false);
        volleyRequest = VolleyRequest.getObject();
        volleyRequest.setContext(getContext());
        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/fetchItems.rfa.php?type=fish");
        volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/fetchItems.rfa.php?type=fish");
        volleyRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) {

                Log.d("Responseitem", response);
                if (!response.contains("nodata")) {
                    try {
                        data = new ArrayList<>();
                        jArray = new JSONArray(response);
                        for (int i = 0; i < jArray.length(); i++) {
                            Log.d("JarrayLength", jArray.length() + "");
                            json_data = jArray.getJSONObject(i);
                            DataCart data_item = new DataCart();
                            json_data = jArray.getJSONObject(i);
                            data_item.name = json_data.getString("itemName");
                            data_item.desc = json_data.getString("description");
                            data_item.price = json_data.getString("price");
                            data_item.id = json_data.getInt("id");
                            data_item.image = json_data.getString("image_path");
                            data.add(data_item);
                        }
                        recyclerView = view.findViewById(R.id.listview_subcategory);
                        recyclerView.setVisibility(VISIBLE);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        recyclerView.setNestedScrollingEnabled(false);
                        recyclerView.setHasFixedSize(false);
                        adapter = new AdapterCart(getContext(), data);
                        Log.d(data + "", "data*************");
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            }
        });
        return view;
    }

}
