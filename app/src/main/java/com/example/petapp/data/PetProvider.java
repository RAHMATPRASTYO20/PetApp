package com.example.petapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class PetProvider extends ContentProvider {

    public static final int PETS = 100;//menampilkan seluruh data di table pets

    public static final int PET_ID = 101;//menampilkan  item spesifik di table sesuai ID item

    //kelas yang digunakan untuk pencocokan code akses table content provider
    private static final UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PETS_PATH, PETS);
        sMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PETS_PATH + "/#", PET_ID);
    }

    private PetDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new PetDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        int match = sMatcher.match(uri);
        switch (match) {
            case PETS :

            return db.query(PetContract.PetEntry.TABLE_NAME,
                    projection,null,null,null,null,null);

            case PET_ID :
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

            return db.query(PetContract.PetEntry.TABLE_NAME,
                    projection,selection,selectionArgs,null,null,null);

            default:
                throw new IllegalArgumentException("Cannot Query unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sMatcher.match(uri);
        switch (match) {
            case PETS :
                return PetContract.PetEntry.CONTENT_LIST_TYPE;
            case PET_ID :
                return PetContract.PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with code match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = sMatcher.match(uri);
        switch (match){
            case PETS :
                return insertPet(uri, values);
            default:
                throw new IllegalArgumentException("Inserting is not supported for URI " + uri);
        }
    }

    //validasi data untuk memastikan data yang dimasukan benar
    private Uri insertPet(Uri uri, ContentValues values) {
        String name = values.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);
        if (name == null || name.isEmpty()) {
            Toast.makeText(getContext(), "Pet requires a valid name", Toast.LENGTH_SHORT).show();
            throw new IllegalArgumentException("Pet requires a valid name");
        }

        Integer gender = values.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);
        if (gender == null || !PetContract.PetEntry.isValidGender(gender)) {
            Toast.makeText(getContext(), "Pet requires a valid gender", Toast.LENGTH_SHORT).show();
            throw new IllegalArgumentException("Pet requires a valid gender");
        }

        Integer weight = values.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);
        if (weight < 0) {
            Toast.makeText(getContext(), "Pet requires a valid weight", Toast.LENGTH_SHORT).show();
            throw new IllegalArgumentException("Pet requires a valid weight");
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(PetContract.PetEntry.TABLE_NAME, null, values);
        if (id == -1){
            Toast.makeText(getContext(), "Error Inserting Data", Toast.LENGTH_SHORT).show();
            return null;
        }

        //hasilnya content://{content authority}/pets/{id}
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        final int match = sMatcher.match(uri);
        switch (match) {
            case PETS:
                // Delete all rows that match the selection and selection args
                return database.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
            case PET_ID:
                // Delete a single row given by the ID in the URI
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return database.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
     return database.update(PetContract.PetEntry.TABLE_NAME, values, selection, selectionArgs);
    }
}
