package br.com.arndroid.etdiet.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public interface ProviderOperator {

    boolean handleUri(Uri uri);

    String getType(Uri uri);

    Cursor query(Uri uri, String[] projection, String selection,
                 String[] selectionArgs, String sortOrder, Provider provider);

    Uri insert(Uri uri, ContentValues values, Provider provider);

    int update(Uri uri, ContentValues values, String selection, String[] selectionArgs,
               Provider provider);

    int delete(Uri uri, String selection, String[] selectionArgs, Provider provider);
}
