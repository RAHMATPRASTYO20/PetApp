package com.example.petapp;

import android.content.ContentValues;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.petapp.data.PetContract;
import com.example.petapp.data.PetDbHelper;

public class EditorActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "petid";
    public static final String EXTRA_NAME = "petname";
    public static final String EXTRA_BREED = "perbreed";
    public static final String EXTRA_WEIGHT = "petweight";
    public static final String EXTRA_GENDER = "petgender";
    public static final String EXTRA_IS_EDIT_MODE = "isEditMode";

    //deklarasi
    private TextInputEditText etName;
    private TextInputEditText etBreed;
    private TextInputEditText etWeight;
    private Spinner spGender;

    private int mGender;
    private String mName;
    private String mBreed;
    private Integer mWeight;

    private PetDbHelper petDbHelper;

    private boolean isEditMode = false;

    private long newRowId;
    private long id;

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

        petDbHelper = new PetDbHelper(this);
        ArrayAdapter genderArrayAdapter = ArrayAdapter
                .createFromResource(this, R.array.array_gender_option,android.R.layout.simple_spinner_item);
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);//untuk menampilkan pilihan kebawah

        spGender.setAdapter(genderArrayAdapter);//untuk mengaktifkan array adapter

        //buat nerima intent dari PetCatalogAdapter
        Intent intent = getIntent();
        if (intent.getExtras()!= null) {
            id = intent.getLongExtra(EXTRA_ID, -1);
            etName.setText(intent.getExtras().getString(EXTRA_NAME));
            etBreed.setText(intent.getExtras().getString(EXTRA_BREED));
            etWeight.setText(String.valueOf(intent.getExtras().getInt(EXTRA_WEIGHT)));
            int position = intent.getExtras().getInt(EXTRA_GENDER);
            spGender.setSelection(position);
            isEditMode = intent.getBooleanExtra(EXTRA_IS_EDIT_MODE, false);
        }



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
                mName = etName.getText().toString();
                mBreed = etBreed.getText().toString();
                mWeight = (etWeight.getText().toString().equals("") ? 0 : Integer.parseInt(etWeight.getText().toString()));
                if (mName.isEmpty() || mBreed.isEmpty()) {
                    etName.setError("Name Can't be Empty");
                    etBreed.setError("Breed Can't be Empty");
                } else {
                    if (isEditMode) {
                        updatePet();
                        Toast.makeText(this, "Data Update", Toast.LENGTH_SHORT).show();
                    }else {
                        savePet();
                        Toast.makeText(this, "Data Inserted", Toast.LENGTH_LONG).show();
                    }
                    finish();
                }
                break;
            case R.id.action_delete:
                deletePet();
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void savePet() {
        ContentValues values = new ContentValues();
        values.put(PetContract.PetEntry.COLUMN_PET_NAME,mName);
        values.put(PetContract.PetEntry.COLUMN_PET_BREED,mBreed);
        values.put(PetContract.PetEntry.COLUMN_PET_GENDER,mGender);
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT,mWeight);
        getContentResolver().insert(PetContract.PetEntry.CONTENT_URI,  values);
    }

    private void deletePet() {
        String[] args = {String.valueOf(id)};
        petDbHelper.getWritableDatabase().delete(PetContract.PetEntry.TABLE_NAME, PetContract.PetEntry._ID+ "=?" , args);
    }

    private void updatePet() {
        ContentValues values = new ContentValues();
        values.put(PetContract.PetEntry.COLUMN_PET_NAME,mName);
        values.put(PetContract.PetEntry.COLUMN_PET_BREED,mBreed);
        values.put(PetContract.PetEntry.COLUMN_PET_GENDER,mGender);
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT,mWeight);
        petDbHelper.getWritableDatabase().update(PetContract.PetEntry.TABLE_NAME,values,
                PetContract.PetEntry._ID + "=" + id, null);
    }
}
