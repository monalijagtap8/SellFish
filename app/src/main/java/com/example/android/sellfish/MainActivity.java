package com.example.android.sellfish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.btnSignIn)Button btnSignIn;
    @InjectView(R.id.btnSignUp)Button btnSignUp;
    @InjectView(R.id.btnSkipSignIn)Button btnSkip;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }
    @OnClick({R.id.btnSignUp,R.id.btnSignIn,R.id.btnSkipSignIn})
    public void onClick(final View view) {

        switch (view.getId()) {

            case R.id.btnSignIn:
                intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSignUp:
                intent=new Intent(MainActivity.this,Registration.class);
                startActivity(intent);
                break;
            case  R.id.btnSkipSignIn:
                break;

        }

    }
}
