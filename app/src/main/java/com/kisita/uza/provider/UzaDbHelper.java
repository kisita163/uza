package com.kisita.uza.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
 * Created by HuguesKi on 03-09-17.
 */

public class UzaDbHelper extends SQLiteOpenHelper {
    static final String  TAG = "### UzaDbHelper";
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 6;

    static final String DATABASE_NAME = "uza.db";

    public UzaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG,"onCreate");
        // Create a table to hold items
        final String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + UzaContract.ItemsEntry.TABLE_NAME + " (" +

                //UzaContract.ItemsEntry._ID                + " INTEGER AUTOINCREMENT," +

                UzaContract.ItemsEntry.COLUMN_NAME        + " TEXT NOT NULL, " +
                UzaContract.ItemsEntry.COLUMN_BRAND       + " TEXT NOT NULL, " +
                UzaContract.ItemsEntry.COLUMN_CATEGORY    + " TEXT NOT NULL, " +
                UzaContract.ItemsEntry.COLUMN_TYPE        + " TEXT NOT NULL, " +
                UzaContract.ItemsEntry.COLUMN_PRICE       + " TEXT NOT NULL, " +
                UzaContract.ItemsEntry.COLUMN_CURRENCY    + " TEXT NOT NULL, " +
                UzaContract.ItemsEntry.COLUMN_SELLER      + " TEXT NOT NULL, " +
                UzaContract.ItemsEntry.COLUMN_PICTURES    + " TEXT NOT NULL, " +
                UzaContract.ItemsEntry._ID                + " TEXT PRIMARY KEY NOT NULL, " +
                UzaContract.ItemsEntry.COLUMN_AID         + " TEXT NOT NULL, " +
                UzaContract.ItemsEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                UzaContract.ItemsEntry.COLUMN_COLOR       + " TEXT NOT NULL, " +
                UzaContract.ItemsEntry.COLUMN_SIZE        + " TEXT NOT NULL, " +
                UzaContract.ItemsEntry.COLUMN_WEIGHT      + " TEXT NOT NULL, " +
                UzaContract.ItemsEntry.COLUMN_URL         + " TEXT NOT NULL" + ");";


        sqLiteDatabase.execSQL(SQL_CREATE_ITEMS_TABLE);

        // Create a table to holds commands
        final String SQL_CREATE_COMMANDS_TABLE = "CREATE TABLE " + UzaContract.CommandsEntry.TABLE_NAME + " (" +

                //UzaContract.CommandsEntry._ID                     + " INTEGER AUTOINCREMENT," +
                UzaContract.CommandsEntry._ID                     + " TEXT PRIMARY KEY NOT NULL," +
                UzaContract.CommandsEntry.COLUMN_COLOR            + " TEXT NOT NULL, " +
                UzaContract.CommandsEntry.COLUMN_SIZE             + " TEXT NOT NULL, " +
                UzaContract.CommandsEntry.COLUMN_KEY              + " TEXT NOT NULL, " +
                UzaContract.CommandsEntry.COLUMN_QUANTITY         + " TEXT NOT NULL, " +
                UzaContract.CommandsEntry.COLUMN_COMMENT          + " TEXT NOT NULL, " +
                UzaContract.CommandsEntry.COLUMN_STATE            + " TEXT NOT NULL" + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_COMMANDS_TABLE);
        // Create a table to holds favourites
        final String SQL_CREATE_LIKES_TABLE = "CREATE TABLE " + UzaContract.LikesEntry.TABLE_NAME + " (" +

                //UzaContract.LikesEntry._ID                     + " INTEGER AUTOINCREMENT," +
                UzaContract.LikesEntry.COLUMN_LIKES            + " TEXT NOT NULL, "  +
                UzaContract.LikesEntry._ID                     + " TEXT PRIMARY KEY NOT NULL " + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_LIKES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UzaContract.ItemsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UzaContract.CommandsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UzaContract.LikesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
