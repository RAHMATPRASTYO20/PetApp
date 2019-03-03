package com.example.petapp;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class EditorActivity extends AppCompatActivity {
    //deklarasi
    private TextInputEditText etName;
    private TextInputEditText etBreed;
    private TextInputEditText etWeight;
    private Spinner spGender;

    private int mGender;
    private String mName;
    private String mBreed;
    private String mWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

            //inisialisasi
        etName = (TextInputEditText) findViewById(R.id.et_name);
        etBreed = (TextInputEditText) findViewById(R.id.et_breed);
        etWeight = (TextInputEditText) findViewById(R.id.et_weight);
        spGender = (Spinner) findViewById(R.id.sp_gender);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//supaya keluar tombol beck di toolbar

        ArrayAdapter genderArrayAdapter = ArrayAdapter
                .createFromResource(this, R.array.array_gender_option,android.R.layout.simple_spinner_item);
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);//untuk menampilkan pilihan kebawah

        spGender.setAdapter(genderArrayAdapter);//untuk mengaktifkan array adapter

        //buat pilihannya seolah olah integer buat di databasenya
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selectedItem)) {
                    switch (selectedItem) {
                        case "Male":
                            mGender = 1;
                            break;
                        case "Female":
                            mGender = 2;
                            break;
                        default:
                            mGender = 0;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0;
            }
        });
    }

    //function menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:

                finish();
                break;
            case R.id.action_delete:
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
