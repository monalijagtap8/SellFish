package com.example.android.sellfish;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    View view;

    @InjectView(R.id.edtName)
    EditText edtName;
    @InjectView(R.id.edtPhone)
    EditText edtPhone;
    @InjectView(R.id.edtEmail)
    EditText edtEmail;
    @InjectView(R.id.edtPassword)
    EditText edtPassword;
    @InjectView(R.id.edtAddress)
    EditText edtAddress;
    @InjectView(R.id.btnRegister)
    Button btnRegister;
    int flag = 0;
    String name, email, phone, password, address;
    VolleyRequest volleyRequest;
    String parent_activity;
    Intent intent;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Bundle bundle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_registration, container, false);
        ButterKnife.inject(this, view);
        sp = getActivity().getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        bundle = this.getArguments();
        parent_activity = bundle.getString("PARENT_ACTIVITY");
        if (parent_activity.equals("UserProfile")) {
            edtPassword.setVisibility(View.GONE);
            btnRegister.setText("update");
            getData();
        }
        return view;
    }

    @OnClick({R.id.btnRegister, R.id.edtName, R.id.edtEmail, R.id.edtPhone, R.id.edtPassword, R.id.edtAddress})
    public void onClick(final View view) {

        switch (view.getId()) {
            case R.id.btnRegister:

                name = edtName.getText().toString().trim();
                if (name.length() == 0) {
                    edtName.setError("Please enter name");
                    flag = 1;
                }
                email = edtEmail.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (emailPattern.matches(email) || email.length() == 0) {
                    edtEmail.setError("Please enter email");
                    flag = 1;
                }
                phone = edtPhone.getText().toString().trim();
                if (phone.length() == 0 || phone.length() < 10) {
                    edtPhone.setError("Please enter phone");
                    flag = 1;
                }
                address = edtAddress.getText().toString().trim();
                if (address.length() == 0) {
                    edtAddress.setError("Please enter address");
                    flag = 1;
                }
                password = edtPassword.getText().toString().trim();
                if (!parent_activity.equals("UserProfile")) {
                    if (password.length() == 0) {
                        edtPassword.setError("Please enter password");
                        flag = 1;
                    }
                } else {
                    password = "-1";
                }
                //flag=0;
                if (flag == 0) {
                    register();
                }
                break;
        }
    }

    public void register() {
        volleyRequest = VolleyRequest.getObject();
        volleyRequest.setContext(getActivity());
        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/userData.rfa.php?name=" + name + "&email=" + email + "&phone=" + phone + "&password=" + password + "&address=" + address);
        volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/userData.rfa.php?name=" + name + "&email=" + email + "&phone=" + phone + "&password=" + password + "&address=" + address);
        volleyRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d("Response", response);
                if (response.contains("OK")) {
                    intent = new Intent(getActivity(), TabActivity.class);
                    getActivity().finish();
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Registration successful", Toast.LENGTH_SHORT).show();
                } else if (response.contains("UPDATED")) {
                    intent = new Intent(getActivity(), UserProfile.class);
                    editor = sp.edit();
                    editor.putString("EMAIL", email);
                    editor.commit();
                    getActivity().finish();
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getData() {

        email = sp.getString("EMAIL", "");
        Log.d("Id", email);
        volleyRequest = VolleyRequest.getObject();
        volleyRequest.setContext(getActivity());
        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/fetchUserProfile.rfa.php?email_id=" + email);
        volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/fetchUserProfile.rfa.php?email_id=" + email);
        volleyRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d("Response*", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    name = jsonObject.getString("name");
                    email = jsonObject.getString("email");
                    phone = jsonObject.getString("phone");
                    password = jsonObject.getString("password");
                    address = jsonObject.getString("address");

                    Log.d("Name", name);

                    edtName.setText(name);
                    edtPhone.setText(phone);
                    edtEmail.setText(email);
                    edtAddress.setText(address);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
