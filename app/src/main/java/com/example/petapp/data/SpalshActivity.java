package com.example.petapp.data;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.petapp.MainActivity;
import com.example.petapp.R;

public class SpalshActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);

        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent s = new Intent(SpalshActivity.this, MainActivity.class);
                startActivity(s);
                finish();
            }
        },2000);
    }
}