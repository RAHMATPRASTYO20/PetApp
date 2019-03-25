package com.example.petapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.petapp.EditorActivity;
import com.example.petapp.R;
import com.example.petapp.data.PetContract;

public class PetCatalogAdapter extends ResourceCursorAdapter {
    private TextView tvName;
    private TextView tvBreed;
    private  int gender;
    private int weight;

    public PetCatalogAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvBreed = (TextView) view.findViewById(R.id.tv_breed);

        final int idColumnIndex = cursor.getColumnIndex(PetContract.PetEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
        int breedColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);
        int genderColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER);
        int weightColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT);

        tvName.setText(cursor.getString(nameColumnIndex));
        tvBreed.setText(cursor.getString(breedColumnIndex));
        gender = cursor.getInt(genderColumnIndex);
        weight = cursor.getInt(weightColumnIndex);
        Log.d("position",String.valueOf(gender));
        //ketika item di click pindah ke editor
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }
}
