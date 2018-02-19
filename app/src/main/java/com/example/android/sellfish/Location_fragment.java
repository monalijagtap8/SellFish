package com.example.android.sellfish;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Location_fragment extends Fragment {

    public Location_fragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);

        ListView list = rootView.findViewById(R.id.listview_location);
        ArrayList<String> locations = new ArrayList<String>();
        locations.add("Bhosari");
        locations.add("Alandi");
        locations.add("Dighi");
        locations.add("chinchwad");
        ArrayAdapter<String> allItemsAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, locations);

        list.setAdapter(allItemsAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getContext(), "Click ListItem Number " + position, Toast.LENGTH_LONG).show();
                Log.d("harshada", getFragmentManager().getBackStackEntryCount() + "");
                //Intent i = new Intent(getContext(),HomeActivity.class);
                //startActivity(i);
                getFragmentManager().popBackStack();
            }
        });


        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();

    }

}
