package com.example.petapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petapp.adapter.PetCatalogAdapter;
import com.example.petapp.data.PetContract;
import com.example.petapp.data.PetDbHelper;

public class MainActivity extends AppCompatActivity {
    private PetDbHelper dbHelper;
    private TextView tvCount;
    private ListView rvCatalog;
    private FloatingActionButton fab;
    private View emptyview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new PetDbHelper(this);
        tvCount = (TextView) findViewById(R.id.tv_count);
        rvCatalog = (ListView) findViewById(R.id.rv_pet_catalog);
        fab = (FloatingActionButton) findViewById(R.id.fabBtn);
        emptyview = (View) findViewById(R.id.empty_view);
        displayDatabaseInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo(){
        String[] projection = {PetContract.PetEntry._ID,
                PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_PET_BREED,
                PetContract.PetEntry.COLUMN_PET_GENDER,
                PetContract.PetEntry.COLUMN_PET_WEIGHT};

//        String selection = PetContract.PetEntry.COLUMN_PET_GENDER + "=?";//sama seperti WHERE di sql
//        String[] selectionArgs = {String.valueOf(PetContract.PetEntry.GENDER_MALE)};
       final Cursor cursor = getContentResolver().query(PetContract.PetEntry.CONTENT_URI, projection, null, null, null);

//        try {
            tvCount.setText("data table pet consists : " + cursor.getCount() + " rows. \n\n");
//            //lanjutan kode diatas
            tvCount.append(PetContract.PetEntry._ID + " - " +
                    PetContract.PetEntry.COLUMN_PET_NAME + " - " +
                    PetContract.PetEntry.COLUMN_PET_BREED + " - " +
                    PetContract.PetEntry.COLUMN_PET_GENDER + " - " +
                    PetContract.PetEntry.COLUMN_PET_WEIGHT + " - " + "\n");

          final int idColumnIndex = cursor.getColumnIndex(PetContract.PetEntry._ID);
          final int nameColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
          final int breedColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);
          final int genderColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER);
          final int weightColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT);
//
//            while (cursor.moveToNext()) {
//                int id = cursor.getInt(idColumnIndex);
//                String name = cursor.getString(nameColumnIndex);
//                String breed = cursor.getString(breedColumnIndex);
//                int gender = cursor.getInt(genderColumnIndex);
//                int weight = cursor.getInt(weightColumnIndex);
//
//                tvCount.append("\n" + id +
//                        " - " + name +
//                        " - " + breed +
//                        " - " + gender +
//                        " - " + weight);
//            }
//        }finally {
//            cursor.close();
//        }
        PetCatalogAdapter adapter = new PetCatalogAdapter(this, R.layout.item_list_pet,cursor,0);
        rvCatalog.setAdapter(adapter);
        adapter.changeCursor(cursor);//buat auto close ketika selesai buat item
        rvCatalog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                //biar data yang dulu dikirim lagi
                intent.putExtra(EditorActivity.EXTRA_ID, cursor.getLong(idColumnIndex));
                intent.putExtra(EditorActivity.EXTRA_NAME,cursor.getString(nameColumnIndex));
                intent.putExtra(EditorActivity.EXTRA_BREED, cursor.getString(breedColumnIndex));
                intent.putExtra(EditorActivity.EXTRA_GENDER, cursor.getInt(genderColumnIndex));
                intent.putExtra(EditorActivity.EXTRA_WEIGHT, cursor.getInt(weightColumnIndex));

                intent.putExtra(EditorActivity.EXTRA_IS_EDIT_MODE, true);

                startActivity(intent);
            }
        });

        if (cursor.getCount() > 0) {
            rvCatalog.setVisibility(View.VISIBLE);
            emptyview.setVisibility(View.GONE);
        }else {
            rvCatalog.setVisibility(View.GONE);
            emptyview.setVisibility(View.VISIBLE);
        }

        adapter.changeCursor(cursor);
    }

    public void onAddPet(View view) {
        Intent editorIntent = new Intent(this, EditorActivity.class);
        startActivity(editorIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void insert() {
        ContentValues cp = new ContentValues();
        cp.put(PetContract.PetEntry.COLUMN_PET_NAME, "Si Meong");
        cp.put(PetContract.PetEntry.COLUMN_PET_BREED, "Kucing");
        cp.put(PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.GENDER_MALE);
        cp.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, 20);
        //bikin sesuatu seperti onCreate
        getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, cp);
//        if (newRowId == -1) {
//            Toast.makeText(this, "Error Inputing Data", Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(this, "Inserting Data Success", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add :
                insert();
                displayDatabaseInfo();
                break;
            case R.id.action_deleteAll :
                deleteAllPet();
                displayDatabaseInfo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllPet() {
        dbHelper.getWritableDatabase().execSQL("delete from " + PetContract.PetEntry.TABLE_NAME);
    }
}
