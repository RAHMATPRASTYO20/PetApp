package com.example.petapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.petapp.R;

public class SignInActivity extends AppCompatActivity {
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();

        btnSignIn = (Button) findViewById(R.id.btn_sing_in);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(move);
            }
        });
    }
}
