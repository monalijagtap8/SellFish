package com.example.android.sellfish;


import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    View view;
    @InjectView(R.id.edtName1)
    EditText edtName;
    @InjectView(R.id.edtPassword1)
    EditText edtPassword;
    @InjectView(R.id.btnLogin)
    Button btnLogin;
    @InjectView(R.id.txtForgotPassword)
    TextView txtForgotPassword;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    VolleyRequest volleyRequest;
    View enquireDialog;
    Intent intent;
    String email, passowrd, name, phone, user_id, pwd1, pwd2, email1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment\
        view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.inject(getContext(), view);

        try {
            File f = new File("/data/data/com.xoxytech.ostello/shared_prefs/YourSharedPreference.xml");
            if (f.exists()) {
                Log.d("TAG", "SharedPreferences Name_of_your_preference : exist");
                sp = getActivity().getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);

            } else
                Log.d("TAG", "Setup default preferences");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @OnClick({R.id.btnLogin, R.id.txtForgotPassword})
    public void onClick(final View view) {

        switch (view.getId()) {

            case R.id.btnLogin:
                login();
                break;
            case R.id.txtForgotPassword:

                LayoutInflater li = LayoutInflater.from(getActivity());

                //Creating a view to get the dialog box
                enquireDialog = li.inflate(R.layout.dialog_forgot_password, null);
                final EditText editEmail = enquireDialog.findViewById(R.id.edtEmail1);
                Button btnSubmit = enquireDialog.findViewById(R.id.btnSubmit);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        email1 = editEmail.getText().toString().trim();
                        getData();
                    }
                });
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                //Adding our dialog box to the view of alert dialog
                alert.setView(enquireDialog);

                //Creating an alert dialog
                final AlertDialog alertDialog = alert.create();
                alertDialog.show();
                break;
        }

    }

    public void getData() {

     /*   email= sp.getString("EMAIL", "");
        Log.d("Id", email);*/
        volleyRequest = VolleyRequest.getObject();
        volleyRequest.setContext(getActivity());
        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/fetchUserProfile.rfa.php?email_id=" + email1);
        volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/fetchUserProfile.rfa.php?email_id=" + email1);
        volleyRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d("Response*", response);

                if (!response.contains("nodata")) {
                    LayoutInflater li = LayoutInflater.from(getActivity());

                    //Creating a view to get the dialog box
                    enquireDialog = li.inflate(R.layout.dialog_reset_password, null);
                    final EditText editNewPass = enquireDialog.findViewById(R.id.edtNewPass);
                    final EditText editConfirmPass = enquireDialog.findViewById(R.id.edtConfirmPass);
                    Button btnReset = enquireDialog.findViewById(R.id.btnReset);
                    btnReset.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pwd1 = editNewPass.getText().toString().trim();
                            pwd2 = editConfirmPass.getText().toString().trim();
                            if (pwd1.equals(pwd2)) {
                                register();
                            } else {
                                Toast.makeText(getActivity(), "Password not matched", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    //Adding our dialog box to the view of alert dialog
                    alert.setView(enquireDialog);

                    //Creating an alert dialog
                    final AlertDialog alertDialog = alert.create();
                    alertDialog.show();

                } else {
                    Toast.makeText(getActivity(), "User does not exists", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    public void register() {

        volleyRequest = VolleyRequest.getObject();
        volleyRequest.setContext(getActivity());
        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/userData.rfa.php?name=NULL" + "&email=" + email1 + "&phone=NULL" + "&password=" + pwd1);
        volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/userData.rfa.php?name=NULL" + "&email=" + email1 + "&phone=NULL" + "&password=" + pwd1);
        volleyRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d("Response", response);
                if (response.contains("OK") || response.contains("UPDATED")) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().finish();
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Password reset successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Password reset failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void login() {
        email = edtName.getText().toString();
        passowrd = edtPassword.getText().toString();

        volleyRequest = VolleyRequest.getObject();
        volleyRequest.setContext(getActivity());

        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/fetchUserData.rfa.php?email=" + email + "&password=" + passowrd);
        volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/fetchUserData.rfa.php?email=" + email + "&password=" + passowrd);
        volleyRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d("Response", response);
                try {
                    if (!response.contains("nodata")) {
                        JSONObject jsonObject = new JSONObject(response);
                        name = jsonObject.getString("name");
                        email = jsonObject.getString("email");
                        phone = jsonObject.getString("phone");
                        user_id = jsonObject.getString("id");

                        editor = sp.edit();
                        editor.putBoolean("LOGGED_IN", true);
                        editor.putString("NAME", name);
                        editor.putString("PHONE", phone);
                        editor.putString("EMAIL", email);
                        editor.putString("USER_ID", user_id);
                        editor.commit();

                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        Toast.makeText(getActivity(), "Login successful...", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), "Login failed...", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
